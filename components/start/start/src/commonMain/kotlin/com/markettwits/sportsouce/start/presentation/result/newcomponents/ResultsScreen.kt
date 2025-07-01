package com.markettwits.sportsouce.start.presentation.result.newcomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@Composable
fun ResultsScreen(
    results: List<MemberResult>,
    lapLabels: Map<Int, String> = emptyMap(),
    onResultClick: (MemberResult) -> Unit = {},
    onSearchQuery: (String) -> Unit = {},
    onDistanceFilter: (String?) -> Unit = {},
    onGenderFilter: (String?) -> Unit = {},
    onTeamFilter: (String?) -> Unit = {},
    onCityFilter: (String?) -> Unit = {},
    onLapFilter: (Int?) -> Unit = {},
    onPageChange: (Int) -> Unit = {},
    searchQuery: String = "",
    selectedDistance: String? = null,
    selectedGender: String? = null,
    selectedTeam: String? = null,
    selectedCity: String? = null,
    selectedLap: Int? = null,
    currentPage: Int = 1,
    totalPages: Int = 1,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val configuration = rememberScreenSizeInfo()
    val isTablet = configuration.wDP.value.dp >= 600.dp
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Поисковая строка
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // Фильтры
        FiltersSection(
            isTablet = isTablet,
            selectedDistance = selectedDistance,
            selectedGender = selectedGender,
            selectedTeam = selectedTeam,
            selectedCity = selectedCity,
            selectedLap = selectedLap,
            onDistanceFilter = onDistanceFilter,
            onGenderFilter = onGenderFilter,
            onTeamFilter = onTeamFilter,
            onCityFilter = onCityFilter,
            onLapFilter = onLapFilter,
            lapLabels = lapLabels,
            results = results,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Основная таблица результатов
        ResultsTable(
            results = results,
            lapLabels = lapLabels,
            selectedLap = selectedLap,
            isTablet = isTablet,
            onResultClick = onResultClick,
            isLoading = isLoading,
            modifier = Modifier.weight(1f)
        )
        
        // Пагинация
        if (totalPages > 1) {
            PaginationSection(
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = onPageChange,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { 
            Text(
                "Поиск по имени или фамилии...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ) 
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Очистить",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

@Composable
private fun FiltersSection(
    isTablet: Boolean,
    selectedDistance: String?,
    selectedGender: String?,
    selectedTeam: String?,
    selectedCity: String?,
    selectedLap: Int?,
    onDistanceFilter: (String?) -> Unit,
    onGenderFilter: (String?) -> Unit,
    onTeamFilter: (String?) -> Unit,
    onCityFilter: (String?) -> Unit,
    onLapFilter: (Int?) -> Unit,
    lapLabels: Map<Int, String>,
    results: List<MemberResult>,
    modifier: Modifier = Modifier
) {
    if (isTablet) {
        TabletFilters(
            selectedDistance = selectedDistance,
            selectedGender = selectedGender,
            selectedTeam = selectedTeam,
            selectedCity = selectedCity,
            selectedLap = selectedLap,
            onDistanceFilter = onDistanceFilter,
            onGenderFilter = onGenderFilter,
            onTeamFilter = onTeamFilter,
            onCityFilter = onCityFilter,
            onLapFilter = onLapFilter,
            lapLabels = lapLabels,
            results = results,
            modifier = modifier
        )
    } else {
        MobileFilters(
            selectedDistance = selectedDistance,
            selectedGender = selectedGender,
            selectedTeam = selectedTeam,
            selectedCity = selectedCity,
            selectedLap = selectedLap,
            onDistanceFilter = onDistanceFilter,
            onGenderFilter = onGenderFilter,
            onTeamFilter = onTeamFilter,
            onCityFilter = onCityFilter,
            onLapFilter = onLapFilter,
            lapLabels = lapLabels,
            results = results,
            modifier = modifier
        )
    }
}

@Composable
private fun TabletFilters(
    selectedDistance: String?,
    selectedGender: String?,
    selectedTeam: String?,
    selectedCity: String?,
    selectedLap: Int?,
    onDistanceFilter: (String?) -> Unit,
    onGenderFilter: (String?) -> Unit,
    onTeamFilter: (String?) -> Unit,
    onCityFilter: (String?) -> Unit,
    onLapFilter: (Int?) -> Unit,
    lapLabels: Map<Int, String>,
    results: List<MemberResult>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Дистанции и пол
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Дистанции как кнопки
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val distances = results.map { it.distance }.distinct()
                
                item {
                    FilterChip(
                        selected = selectedDistance == null,
                        onClick = { onDistanceFilter(null) },
                        label = { Text("Все дистанции") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
                
                items(distances) { distance ->
                    FilterChip(
                        selected = selectedDistance == distance,
                        onClick = { onDistanceFilter(distance) },
                        label = { Text(distance) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
            
            // Фильтр по полу
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedGender == null,
                    onClick = { onGenderFilter(null) },
                    label = { Text("Все") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                FilterChip(
                    selected = selectedGender == "М",
                    onClick = { onGenderFilter("М") },
                    label = { Text("М") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                FilterChip(
                    selected = selectedGender == "Ж",
                    onClick = { onGenderFilter("Ж") },
                    label = { Text("Ж") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Дополнительные фильтры
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Команды
            item {
                TeamFilterDropdown(
                    selectedTeam = selectedTeam,
                    onTeamFilter = onTeamFilter,
                    teams = results.map { it.team }.distinct()
                )
            }
            
            // Города (если есть в модели)
            item {
                CityFilterDropdown(
                    selectedCity = selectedCity,
                    onCityFilter = onCityFilter,
                    cities = emptyList() // Добавить поле city в модель
                )
            }
            
            // Фильтр по кругам с информацией о количестве результатов
            item {
                LapFilterDropdown(
                    selectedLap = selectedLap,
                    onLapFilter = onLapFilter,
                    lapLabels = lapLabels,
                    results = results
                )
            }
        }
    }
}

@Composable
private fun MobileFilters(
    selectedDistance: String?,
    selectedGender: String?,
    selectedTeam: String?,
    selectedCity: String?,
    selectedLap: Int?,
    onDistanceFilter: (String?) -> Unit,
    onGenderFilter: (String?) -> Unit,
    onTeamFilter: (String?) -> Unit,
    onCityFilter: (String?) -> Unit,
    onLapFilter: (Int?) -> Unit,
    lapLabels: Map<Int, String>,
    results: List<MemberResult>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Дистанции как выпадающий список и пол как кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DistanceFilterDropdown(
                selectedDistance = selectedDistance,
                onDistanceFilter = onDistanceFilter,
                distances = results.map { it.distance }.distinct(),
                modifier = Modifier.weight(1f)
            )
            
            // Фильтр по полу
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(
                    selected = selectedGender == null,
                    onClick = { onGenderFilter(null) },
                    label = { Text("Все", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                )
                FilterChip(
                    selected = selectedGender == "М",
                    onClick = { onGenderFilter("М") },
                    label = { Text("М", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                )
                FilterChip(
                    selected = selectedGender == "Ж",
                    onClick = { onGenderFilter("Ж") },
                    label = { Text("Ж", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.defaultMinSize(minHeight = 32.dp)
                )
            }
        }
        
        // Дополнительные фильтры
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TeamFilterDropdown(
                selectedTeam = selectedTeam,
                onTeamFilter = onTeamFilter,
                teams = results.map { it.team }.distinct(),
                modifier = Modifier.weight(1f)
            )
            
            LapFilterDropdown(
                selectedLap = selectedLap,
                onLapFilter = onLapFilter,
                lapLabels = lapLabels,
                results = results,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DistanceFilterDropdown(
    selectedDistance: String?,
    onDistanceFilter: (String?) -> Unit,
    distances: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedDistance ?: "Все дистанции",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Все дистанции") },
                onClick = {
                    onDistanceFilter(null)
                    expanded = false
                }
            )
            distances.forEach { distance ->
                DropdownMenuItem(
                    text = { Text(distance) },
                    onClick = {
                        onDistanceFilter(distance)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamFilterDropdown(
    selectedTeam: String?,
    onTeamFilter: (String?) -> Unit,
    teams: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedTeam ?: "Все команды",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Все команды") },
                onClick = {
                    onTeamFilter(null)
                    expanded = false
                }
            )
            teams.forEach { team ->
                DropdownMenuItem(
                    text = { Text(team) },
                    onClick = {
                        onTeamFilter(team)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityFilterDropdown(
    selectedCity: String?,
    onCityFilter: (String?) -> Unit,
    cities: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCity ?: "Все города",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Все города") },
                onClick = {
                    onCityFilter(null)
                    expanded = false
                }
            )
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCityFilter(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LapFilterDropdown(
    selectedLap: Int?,
    onLapFilter: (Int?) -> Unit,
    lapLabels: Map<Int, String>,
    results: List<MemberResult>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Подсчет результатов по отметкам
    val lapCounts = remember(results) {
        results.flatMap { it.circles.keys }
            .groupingBy { it }
            .eachCount()
    }
    
    val displayText = when {
        selectedLap == null -> "Финальный результат"
        lapLabels.containsKey(selectedLap) -> lapLabels[selectedLap]!!
        else -> "Круг $selectedLap"
    }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { 
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Финальный результат")
                        Text(
                            text = "${results.count { it.result.isNotBlank() }}",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 12.sp
                        )
                    }
                },
                onClick = {
                    onLapFilter(null)
                    expanded = false
                }
            )
            
            lapLabels.toMap().forEach { (lapNumber, lapLabel) ->
                DropdownMenuItem(
                    text = { 
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(lapLabel)
                            Text(
                                text = "${lapCounts[lapNumber] ?: 0}",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp
                            )
                        }
                    },
                    onClick = {
                        onLapFilter(lapNumber)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultsTable(
    results: List<MemberResult>,
    lapLabels: Map<Int, String>,
    selectedLap: Int?,
    isTablet: Boolean,
    onResultClick: (MemberResult) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }
        return
    }
    
    if (results.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
                Text(
                    text = "Результаты не найдены",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Попробуйте изменить фильтры поиска",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
        return
    }
    
    val scrollState = rememberScrollState()
    
    Column(modifier = modifier) {
        // Заголовок таблицы
        TableHeader(
            lapLabels = lapLabels,
            selectedLap = selectedLap,
            isTablet = isTablet,
            scrollState = scrollState
        )
        
        // Основная таблица с прокруткой
        LazyColumn {
            items(results) { result ->
                ResultRow(
                    result = result,
                    lapLabels = lapLabels,
                    selectedLap = selectedLap,
                    isTablet = isTablet,
                    scrollState = scrollState,
                    onClick = { onResultClick(result) }
                )
            }
        }
    }
}

@Composable
private fun TableHeader(
    lapLabels: Map<Int, String>,
    selectedLap: Int?,
    isTablet: Boolean,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Место
            Text(
                text = "№",
                fontSize = if (isTablet) 14.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.width(40.dp)
            )
            
            // Имя
            Text(
                text = "Участник",
                fontSize = if (isTablet) 14.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.width(if (isTablet) 200.dp else 150.dp)
            )
            
            // Команда
            Text(
                text = "Команда",
                fontSize = if (isTablet) 14.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.width(if (isTablet) 150.dp else 120.dp)
            )
            
            if (selectedLap == null) {
                // Показываем все круги + финальный результат
                lapLabels.toMap().forEach { (lapNumber, lapLabel) ->
                    Text(
                        text = lapLabel,
                        fontSize = if (isTablet) 14.sp else 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.width(if (isTablet) 100.dp else 80.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                Text(
                    text = "Результат",
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.width(if (isTablet) 120.dp else 100.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                // Показываем только выбранный круг
                val lapLabel = lapLabels[selectedLap] ?: "Круг $selectedLap"
                Text(
                    text = lapLabel,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.width(if (isTablet) 120.dp else 100.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ResultRow(
    result: MemberResult,
    lapLabels: Map<Int, String>,
    selectedLap: Int?,
    isTablet: Boolean,
    scrollState: ScrollState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = when (result.place) {
                1 -> Color(0xFFFFD700).copy(alpha = 0.1f) // Золото
                2 -> Color(0xFFC0C0C0).copy(alpha = 0.1f) // Серебро
                3 -> Color(0xFFCD7F32).copy(alpha = 0.1f) // Бронза
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Место с медалькой
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                when (result.place) {
                    1 -> Text("🥇", fontSize = if (isTablet) 20.sp else 16.sp)
                    2 -> Text("🥈", fontSize = if (isTablet) 20.sp else 16.sp)
                    3 -> Text("🥉", fontSize = if (isTablet) 20.sp else 16.sp)
                    else -> Text(
                        text = result.place.toString(),
                        fontSize = if (isTablet) 14.sp else 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // Имя и номер
            Column(
                modifier = Modifier.width(if (isTablet) 200.dp else 150.dp)
            ) {
                Text(
                    text = result.name,
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "#${result.bodyNumber}",
                    fontSize = if (isTablet) 12.sp else 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Команда
            Text(
                text = result.team,
                fontSize = if (isTablet) 14.sp else 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(if (isTablet) 150.dp else 120.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (selectedLap == null) {
                lapLabels.toMap().forEach { (lapNumber, _) ->
                    Text(
                        text = result.circles[lapNumber]?.ifBlank { "--:--:--" } ?: "--:--:--",
                        fontSize = if (isTablet) 13.sp else 11.sp,
                        color = if (result.circles[lapNumber]?.isNotBlank() == true) 
                            MaterialTheme.colorScheme.secondary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.width(if (isTablet) 100.dp else 80.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Text(
                    text = result.result.ifBlank { "--:--:--" },
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = if (result.result.isNotBlank()) 
                        MaterialTheme.colorScheme.secondary 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.width(if (isTablet) 120.dp else 100.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            } else {
                // Показываем только выбранный круг
                Text(
                    text = result.circles[selectedLap]?.ifBlank { "--:--:--" } ?: "--:--:--",
                    fontSize = if (isTablet) 14.sp else 12.sp,
                    color = if (result.circles[selectedLap]?.isNotBlank() == true) 
                        MaterialTheme.colorScheme.secondary 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.width(if (isTablet) 120.dp else 100.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PaginationSection(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка "Предыдущая"
            TextButton(
                onClick = { onPageChange(currentPage - 1) },
                enabled = currentPage > 1,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text("Предыдущая")
            }
            
            // Индикатор страниц
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val startPage = maxOf(1, currentPage - 2)
                val endPage = minOf(totalPages, currentPage + 2)
                
                if (startPage > 1) {
                    PageButton(
                        page = 1,
                        isCurrentPage = false,
                        onClick = { onPageChange(1) }
                    )
                    if (startPage > 2) {
                        Text(
                            text = "...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                for (page in startPage..endPage) {
                    PageButton(
                        page = page,
                        isCurrentPage = page == currentPage,
                        onClick = { onPageChange(page) }
                    )
                }
                
                if (endPage < totalPages) {
                    if (endPage < totalPages - 1) {
                        Text(
                            text = "...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    PageButton(
                        page = totalPages,
                        isCurrentPage = false,
                        onClick = { onPageChange(totalPages) }
                    )
                }
            }
            
            // Кнопка "Следующая"
            TextButton(
                onClick = { onPageChange(currentPage + 1) },
                enabled = currentPage < totalPages,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Следующая")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun PageButton(
    page: Int,
    isCurrentPage: Boolean,
    onClick: () -> Unit
) {
    if (isCurrentPage) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() },
            color = Color.Transparent,
            border = BorderStroke(
                1.dp, 
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}