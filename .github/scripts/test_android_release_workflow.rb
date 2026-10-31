require 'minitest/autorun'
require 'yaml'
require 'tmpdir'
require 'fileutils'
require 'open3'

class AndroidReleaseWorkflowTest < Minitest::Test
  def setup
    @jobs = YAML.load_file(File.join(__dir__, '../workflows/app-release.yml')).fetch('jobs')
  end

  def dependencies(job)
    Array(@jobs.fetch(job)['needs'])
  end

  def test_public_github_body_stays_english_while_app_snapshots_are_separate
    script = @jobs.fetch('prepare-release').fetch('steps')
                  .find { |step| step['name'] == 'Prepare release notes' }.fetch('run')
                  .gsub('${{ steps.version.outputs.version_name }}', '1.2.3')
    Dir.mktmpdir do |dir|
      notes = File.join(dir, '.github/whatsnew')
      FileUtils.mkdir_p(notes)
      File.write(File.join(notes, 'whatsnew-en-US'), 'English notes $APP_VERSION')
      File.write(File.join(notes, 'whatsnew-ru-RU'), 'Русский текст $APP_VERSION')
      output = File.join(dir, 'output')
      _, stderr, status = Open3.capture3({ 'GITHUB_OUTPUT' => output },
        'bash', '-e', '-o', 'pipefail', '-c', script, chdir: dir)
      assert status.success?, stderr
      assert_includes File.read(output), 'English notes 1.2.3'
      refute_includes File.read(output), 'Русский текст'
    end
    publish = @jobs.fetch('publish-github-release').fetch('steps')
                   .find { |step| step['name'] == 'Verify and publish complete release' }
    assert_includes publish.fetch('run'), 'release-notes/whatsnew-ru-RU.txt'
    assert_includes publish.fetch('run'), 'release-notes/whatsnew-en-US.txt'
  end

  def ancestors(job)
    dependencies(job).flat_map { |dependency| [dependency, *ancestors(dependency)] }.uniq
  end

  def test_play_is_independent_of_github_and_desktop_jobs
    assert_includes ancestors('deploy-to-google-play'), 'build-android-google-play-release'
    %w[build-android-github-release build-desktop-macos-release build-desktop-windows-release publish-github-release].each do |job|
      refute_includes ancestors('deploy-to-google-play'), job
    end
    assert_includes ancestors('publish-github-release'), 'build-android-github-release'
    refute_includes ancestors('publish-github-release'), 'build-android-google-play-release'
  end

  def test_each_channel_builds_and_validates_its_own_artifact
    {
      'build-android-google-play-release' => ['bundleGooglePlayRelease', 'googlePlayRelease', 'android-aab'],
      'build-android-github-release' => ['assembleGithubRelease', 'githubRelease', 'android-apk'],
    }.each do |job, (task, variant, artifact)|
      steps = @jobs.fetch(job).fetch('steps')
      assert steps.any? { |step| step.fetch('run', '').include?(task) }
      assert steps.any? { |step| step.fetch('run', '').include?("verify_android_manifest.py #{variant}") }
      upload = steps.find { |step| step.dig('with', 'name') == artifact }
      assert_equal 'error', upload.fetch('with').fetch('if-no-files-found')
    end
  end

  def test_signing_continuity_gate_only_blocks_github_artifact
    github_steps = @jobs.fetch('build-android-github-release').fetch('steps')
    gate = github_steps.index { |step| step['name'] == 'Verify GitHub APK signing continuity' }
    upload = github_steps.index { |step| step.dig('with', 'name') == 'android-apk' }
    refute_nil gate
    refute_nil upload
    assert_operator gate, :<, upload
    assert github_steps[gate].fetch('run').include?('apksigner')
    assert github_steps[gate].fetch('run').include?('verify_apk_upgrade.py')
    refute @jobs.fetch('build-android-google-play-release').fetch('steps').any? { |step| step['name'] == 'Verify GitHub APK signing continuity' }
  end

  def test_apk_identity_gate_runs_before_github_upload
    steps = @jobs.fetch('build-android-github-release').fetch('steps')
    gate = steps.index { |step| step['name'] == 'Verify GitHub APK package and version' }
    upload = steps.index { |step| step.dig('with', 'name') == 'android-apk' }
    refute_nil gate
    assert_operator gate, :<, upload
    assert_includes steps[gate].fetch('run'), 'verify_github_apk.py'
  end

  def test_android_can_publish_when_desktop_fails
    job = @jobs.fetch('publish-github-release')
    assert_includes job.fetch('if'), '!cancelled()'
    assert_includes job.fetch('if'), "needs.build-android-github-release.result == 'success'"
    refute_includes job.fetch('if'), "needs.build-desktop-macos-release.result == 'success'"
    refute_includes job.fetch('if'), "needs.build-desktop-windows-release.result == 'success'"
    steps = job.fetch('steps')
    %w[Download\ DMG\ artifact Download\ EXE\ artifact].each do |name|
      step = steps.find { |item| item['name'] == name }
      refute_nil step
      assert_includes step.fetch('if'), "needs.build-desktop-macos-release.result == 'success'"
      assert_includes step.fetch('if'), "needs.build-desktop-windows-release.result == 'success'"
      assert_equal true, step.fetch('continue-on-error')
    end
    resolver = steps.find { |item| item['name'] == 'Resolve release assets' }
    assert_includes resolver.fetch('env').fetch('DESKTOP_READY'), 'steps.desktop_dmg_download.outcome'
    assert_includes resolver.fetch('env').fetch('DESKTOP_READY'), 'steps.desktop_exe_download.outcome'
    assert_includes resolver.fetch('run'), 'desktop_ready=$DESKTOP_READY'
    inputs = steps.find { |item| item['name'] == 'Generate update manifest' }
    assert_includes inputs.fetch('run'), "desktop_ready=false"
    publish = steps.find { |item| item['name'] == 'Verify and publish complete release' }
    assert_includes publish.fetch('run'), 'assets=("$APK_PATH")'
  end

  def test_asset_resolution_falls_back_without_desktop_files
    script = @jobs.fetch('publish-github-release').fetch('steps')
                  .find { |step| step['name'] == 'Resolve release assets' }.fetch('run')
    Dir.mktmpdir do |dir|
      FileUtils.mkdir_p(File.join(dir, 'artifacts/android-apk'))
      File.write(File.join(dir, 'artifacts/android-apk/app.apk'), 'apk')
      output = File.join(dir, 'output')
      [false, true].each do |desktop_ready|
        File.write(output, '')
        env = { 'GITHUB_OUTPUT' => output, 'DESKTOP_READY' => desktop_ready.to_s }
        _, stderr, status = Open3.capture3(env, 'bash', '-e', '-o', 'pipefail', '-c', script, chdir: dir)
        assert status.success?, stderr
        assert_includes File.read(output), 'desktop_ready=false'
        assert_includes File.read(output), 'apk_path='
      end
      { 'desktop-dmg' => 'app.dmg', 'desktop-exe' => 'app.exe' }.each do |folder, name|
        FileUtils.mkdir_p(File.join(dir, 'artifacts', folder))
        path = File.join(dir, 'artifacts', folder, name)
        File.write(path, 'package')
        File.write("#{path}.architecture", 'arm64')
      end
      File.write(output, '')
      _, stderr, status = Open3.capture3({ 'GITHUB_OUTPUT' => output, 'DESKTOP_READY' => 'true' },
                                         'bash', '-e', '-o', 'pipefail', '-c', script, chdir: dir)
      assert status.success?, stderr
      assert_includes File.read(output), 'desktop_ready=true'
      assert_includes File.read(output), 'dmg_path='
      assert_includes File.read(output), 'exe_path='
    end
  end
end
