// Test-only HTTP endpoints. Production sources still use the real browser Fetch API.
config.plugins.push({
    'middleware:webDeploymentFixture': ['factory', function () {
        const counts = new Map();
        return function (request, response, next) {
            const url = new URL(request.url, 'http://localhost');
            if (!url.pathname.startsWith('/__updater_qa__/')) return next();
            const scenario = url.pathname.split('/')[2];
            const count = (counts.get(scenario) || 0) + 1;
            counts.set(scenario, count);
            response.setHeader('Content-Type', 'application/json; charset=utf-8');
            // Deliberately cacheable: each check must fetch the current response anyway.
            response.setHeader('Cache-Control', 'public, max-age=3600');
            if (scenario === 'unavailable') {
                response.statusCode = 503;
                return response.end('Unavailable');
            }
            if (scenario === 'html') return response.end('<!doctype html><title>Not found</title>');
            if (scenario === 'malformed') return response.end('{"sequence":');
            if (scenario === 'oversized') return response.end(' '.repeat(131073));
            if (scenario === 'slow' || scenario === 'cancel') {
                const timer = setTimeout(() => response.end('{}'), 30000);
                response.on('close', () => clearTimeout(timer));
                return;
            }
            const sequence = scenario === 'changing' ? 11 + count :
                scenario === 'rollback' && count > 1 ? 11 : 12;
            response.end(JSON.stringify({
                schemaVersion: scenario === 'schema' ? 2 : 1,
                sequence, versionName: '1.6.1', buildNumber: 10422,
                changes: {ru: 'Исправления браузерной версии', en: 'Browser fixes'},
                futureField: 'Older clients must tolerate additive metadata fields'
            }));
        };
    }]
});
config.middleware = (config.middleware || []).concat('webDeploymentFixture');
config.client.mocha = Object.assign({}, config.client.mocha, {timeout: 15000});
