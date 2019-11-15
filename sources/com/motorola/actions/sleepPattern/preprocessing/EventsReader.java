package com.motorola.actions.sleepPattern.preprocessing;

import com.motorola.actions.sleepPattern.common.TimeSlot;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EventsReader {
    private static final int MAX_EVENTS_AWAKE = 500;
    private SleepPatternRepository mSleepPatternRepository;
    private SortedMap<Calendar, Integer> mWeekData = new TreeMap();
    private SortedMap<Calendar, Integer> mWeekendData = new TreeMap();

    public EventsReader(SleepPatternRepository sleepPatternRepository) {
        this.mSleepPatternRepository = sleepPatternRepository;
    }

    public void readAccelerometerLogs(TimeZone timeZone) {
        List allAccelerometerDailyLogs = this.mSleepPatternRepository.getAllAccelerometerDailyLogs();
        Map map = (Map) allAccelerometerDailyLogs.stream().collect(Collectors.toMap(new EventsReader$$Lambda$0(timeZone), EventsReader$$Lambda$1.$instance));
        if (allAccelerometerDailyLogs.size() > 0) {
            for (TimeSlot timeSlot : getTimeSlotsOfInterval(new TimeSlot(timeZone, ((AccelEntity) allAccelerometerDailyLogs.get(0)).getTimeSlotId()))) {
                Calendar halfTime = timeSlot.getHalfTime();
                int min = Math.min(((Integer) map.getOrDefault(timeSlot, Integer.valueOf(0))).intValue(), MAX_EVENTS_AWAKE);
                if (Utils.isWeekend(halfTime)) {
                    this.mWeekendData.put(halfTime, Integer.valueOf(min));
                } else {
                    this.mWeekData.put(halfTime, Integer.valueOf(min));
                }
            }
        }
    }

    static final /* synthetic */ TimeSlot lambda$readAccelerometerLogs$0$EventsReader(TimeZone timeZone, AccelEntity accelEntity) {
        return new TimeSlot(timeZone, accelEntity.getTimeSlotId());
    }

    public List<TimeSlot> getTimeSlotsOfInterval(TimeSlot timeSlot) {
        ArrayList arrayList = new ArrayList(36);
        List intervalSetForCalendar = Utils.getIntervalSetForCalendar(timeSlot.getBeginTime());
        if (intervalSetForCalendar.size() == 2) {
            TimeSlot timeSlot2 = new TimeSlot((Calendar) intervalSetForCalendar.get(0));
            TimeSlot timeSlot3 = new TimeSlot((Calendar) intervalSetForCalendar.get(1));
            for (long j = timeSlot2.get(); j < timeSlot3.get(); j++) {
                arrayList.add(new TimeSlot(timeSlot.getTimeZone(), j));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public List<List<Calendar>> getGroupIntervalSets(SortedMap<Calendar, Integer> sortedMap) {
        LinkedList linkedList = new LinkedList();
        if (sortedMap.entrySet().size() > 0) {
            Iterator it = sortedMap.keySet().iterator();
            while (it.hasNext()) {
                List intervalSetForCalendar = Utils.getIntervalSetForCalendar((Calendar) it.next());
                linkedList.add(intervalSetForCalendar);
                it = sortedMap.tailMap(intervalSetForCalendar.get(1)).keySet().iterator();
            }
        }
        return linkedList;
    }

    public SortedMap<Calendar, Integer> getWeekData() {
        return this.mWeekData;
    }

    public SortedMap<Calendar, Integer> getWeekendData() {
        return this.mWeekendData;
    }
}
