package com.yumedev.seijakulistkmp.features.schedule.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.schedule.domain.model.ScheduleDay
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Calendar
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DayStickyHeader(
    scheduleDay: ScheduleDay,
    timezone: TimeZone,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.Calendar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = getDayName(scheduleDay.dayOfWeek),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatDate(scheduleDay.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (scheduleDay.isToday) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.schedule_today),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun getDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(Res.string.day_monday)
        DayOfWeek.TUESDAY -> stringResource(Res.string.day_tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.day_wednesday)
        DayOfWeek.THURSDAY -> stringResource(Res.string.day_thursday)
        DayOfWeek.FRIDAY -> stringResource(Res.string.day_friday)
        DayOfWeek.SATURDAY -> stringResource(Res.string.day_saturday)
        DayOfWeek.SUNDAY -> stringResource(Res.string.day_sunday)
    }
}

@Composable
private fun formatDate(date: LocalDate): String {
    val month = when (date.monthNumber) {
        1 -> stringResource(Res.string.month_january)
        2 -> stringResource(Res.string.month_february)
        3 -> stringResource(Res.string.month_march)
        4 -> stringResource(Res.string.month_april)
        5 -> stringResource(Res.string.month_may)
        6 -> stringResource(Res.string.month_june)
        7 -> stringResource(Res.string.month_july)
        8 -> stringResource(Res.string.month_august)
        9 -> stringResource(Res.string.month_september)
        10 -> stringResource(Res.string.month_october)
        11 -> stringResource(Res.string.month_november)
        12 -> stringResource(Res.string.month_december)
        else -> ""
    }
    return "$month ${date.dayOfMonth}, ${date.year}"
}
