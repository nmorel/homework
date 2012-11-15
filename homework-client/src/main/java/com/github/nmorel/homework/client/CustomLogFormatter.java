package com.github.nmorel.homework.client;

import java.util.Date;
import java.util.logging.LogRecord;

import com.google.common.base.Strings;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.logging.impl.FormatterImpl;

public class CustomLogFormatter
    extends FormatterImpl
{
    private boolean showStackTraces;
    private final DateTimeFormat dtFormat = DateTimeFormat.getFormat( "hh:mm:ss.SSS" );

    public CustomLogFormatter( boolean showStackTraces )
    {
        this.showStackTraces = showStackTraces;
    }

    @Override
    public String format( LogRecord event )
    {
        StringBuilder message = new StringBuilder();
        message.append( getRecordInfo( event, " " ) );
        message.append( event.getMessage() );
        if ( showStackTraces )
        {
            message.append( getStackTraceAsString( event.getThrown(), "\n", "\t" ) );
        }
        return message.toString();
    }

    @Override
    protected String getRecordInfo( LogRecord event, String newline )
    {
        Date date = new Date( event.getMillis() );
        StringBuilder s = new StringBuilder();
        s.append( dtFormat.format( date ) );
        s.append( " [" );
        s.append( Strings.padEnd( event.getLevel().getName(), 7, ' ' ) );
        s.append( "] " );
        s.append( event.getLoggerName() );
        s.append( " : " );
        return s.toString();
    }
}
