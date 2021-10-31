package com.itechart.library.scheduler.trigger;

import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.util.Date;

public class TriggerFactory {
    public static Trigger createSimpleTrigger(TriggerKey triggerKey, Date triggerDate) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(triggerDate)
                .build();
    }
}
