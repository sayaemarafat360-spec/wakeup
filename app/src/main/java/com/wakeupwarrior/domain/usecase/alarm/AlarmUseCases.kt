package com.wakeupwarrior.domain.usecase.alarm

import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.core.util.calculateNextAlarmTime
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAllAlarms()
}

class GetEnabledAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getEnabledAlarms()
}

class GetAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Long): Alarm? = repository.getAlarmById(id)
}

class CreateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository,
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(alarm: Alarm): Result<Long> {
        return try {
            val stats = statsRepository.getStatsOnce()
            val alarmCount = repository.getAlarmCount()
            
            // Check if user can create more alarms
            if (stats?.hasValidPremium != true && alarmCount >= Constants.Alarm.MAX_FREE_ALARMS) {
                return Result.failure(Exception("Free users can only have ${Constants.Alarm.MAX_FREE_ALARMS} alarms. Upgrade to Premium for unlimited alarms."))
            }
            
            val id = repository.createAlarm(alarm)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.updateAlarm(alarm.copy(updatedAt = System.currentTimeMillis()))
    }
}

class DeleteAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.deleteAlarm(alarm)
    }
}

class ToggleAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Long, enabled: Boolean) {
        repository.setAlarmEnabled(id, enabled)
    }
}

class GetNextAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(): Alarm? {
        val alarms = repository.getEnabledAlarmsList()
        return alarms.minByOrNull { alarm ->
            calculateNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
        }
    }
}

class IncrementSnoozeUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarmId: Long): Boolean {
        val alarm = repository.getAlarmById(alarmId) ?: return false
        return if (alarm.snoozesUsed < alarm.snoozeLimit) {
            repository.incrementSnoozes(alarmId)
            true
        } else {
            false
        }
    }
}

class ResetSnoozesUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarmId: Long) {
        repository.resetSnoozes(alarmId)
    }
}
