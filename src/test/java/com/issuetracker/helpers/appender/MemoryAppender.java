package com.issuetracker.helpers.appender;
//
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.AppenderBase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MemoryAppender extends AppenderBase<ILoggingEvent> {
//    private final List<ILoggingEvent> logEvents = new ArrayList<>();
//
//    @Override
//    protected void append(ILoggingEvent eventObject) {
//        logEvents.add(eventObject);
//    }
//
//    public List<ILoggingEvent> getLogEvents() {
//        return logEvents;
//    }
//}
