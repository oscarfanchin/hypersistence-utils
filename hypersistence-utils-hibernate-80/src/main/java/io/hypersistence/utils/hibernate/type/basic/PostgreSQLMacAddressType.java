package io.hypersistence.utils.hibernate.type.basic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.WrapperOptions;

import io.hypersistence.utils.common.ReflectionUtils;
import io.hypersistence.utils.hibernate.type.ImmutableType;
import io.hypersistence.utils.hibernate.type.util.Configuration;

/**
 * Maps a {@link MacAddress} object type to a PostgreSQL {@code macaddr} column type.
 */
public class PostgreSQLMacAddressType extends ImmutableType<MacAddress> {

    public static final PostgreSQLMacAddressType INSTANCE = new PostgreSQLMacAddressType();

    public PostgreSQLMacAddressType() {
        super(MacAddress.class);
    }

    public PostgreSQLMacAddressType(org.hibernate.type.spi.TypeBootstrapContext typeBootstrapContext) {
        super(MacAddress.class, new Configuration(typeBootstrapContext.getConfigurationSettings()));
    }

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public MacAddress get(ResultSet rs, int position,
        WrapperOptions options) throws SQLException {
        String macAddress = rs.getString(position);
        return (macAddress != null) ? new MacAddress(macAddress) : null;
    }

    @Override
    public void set(PreparedStatement st, MacAddress value, int index,WrapperOptions options) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            Object holder = ReflectionUtils.newInstance("org.postgresql.util.PGobject");
            ReflectionUtils.invokeSetter(holder, "type", "macaddr");
            ReflectionUtils.invokeSetter(holder, "value", value.getAddress());
            st.setObject(index, holder);
        }
    }

    @Override
    public MacAddress fromStringValue(CharSequence sequence) throws HibernateException {
        return sequence != null ? new MacAddress((String) sequence) : null;
    }
}
