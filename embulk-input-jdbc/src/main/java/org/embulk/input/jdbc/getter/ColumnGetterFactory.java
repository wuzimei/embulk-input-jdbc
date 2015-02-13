package org.embulk.input.jdbc.getter;

import java.sql.Types;
import org.embulk.input.jdbc.JdbcColumn;
import org.embulk.input.jdbc.getter.ColumnGetters.BooleanColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.LongColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.DoubleColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.StringColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.DateColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.TimeColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetters.TimestampColumnGetter;

public class ColumnGetterFactory
{
    public ColumnGetter newColumnGetter(JdbcColumn column)
    {
        switch(column.getSqlType()) {
        // getLong
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return new LongColumnGetter();

        // setDouble
        case Types.DOUBLE:
        case Types.FLOAT:
        case Types.REAL:
            return new DoubleColumnGetter();

        // setBool
        case Types.BOOLEAN:
        case Types.BIT:  // JDBC BIT is boolean, unlike SQL-92
            return new BooleanColumnGetter();

        // setString, Clob
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.CLOB:
        case Types.NCHAR:
        case Types.NVARCHAR:
        case Types.LONGNVARCHAR:
            return new StringColumnGetter();

        // TODO
        //// setBytes Blob
        //case Types.BINARY:
        //case Types.VARBINARY:
        //case Types.LONGVARBINARY:
        //case Types.BLOB:
        //    return new BytesColumnGetter();

        // getDate
        case Types.DATE:
            return new DateColumnGetter(); // TODO

        // getTime
        case Types.TIME:
            return new TimeColumnGetter(); // TODO

        // getTimestamp
        case Types.TIMESTAMP:
            return new TimestampColumnGetter();

        // TODO
        //// Null
        //case Types.NULL:
        //    return new NullColumnGetter();

        // TODO
        //// BigDecimal
        //case Types.NUMERIC:
        //case Types.DECIMAL:
        //    return new BigDecimalColumnGetter();

        // others
        case Types.ARRAY:  // array
        case Types.STRUCT: // map
        case Types.REF:
        case Types.DATALINK:
        case Types.SQLXML: // XML
        case Types.ROWID:
        case Types.DISTINCT:
        case Types.JAVA_OBJECT:
        case Types.OTHER:
        default:
            throw unsupportedOperationException(column);
        }
    }

    private static UnsupportedOperationException unsupportedOperationException(JdbcColumn column)
    {
        throw new UnsupportedOperationException(
                String.format("Unsupported type %s (sqlType=%d)",
                    column.getTypeName(), column.getSqlType()));
    }
}

