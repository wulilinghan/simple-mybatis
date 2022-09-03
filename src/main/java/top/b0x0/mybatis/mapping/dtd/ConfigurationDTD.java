package top.b0x0.mybatis.mapping.dtd;

import java.util.Arrays;
import java.util.List;

/**
 * @author tlh Created By 2022-07-31 20:55
 **/
public class ConfigurationDTD {

    public static final String ELE_configuration = "configuration";
    public static final String ELE_configuration_ATT_properties = "properties";
    public static final String ELE_configuration_ATT_settings = "settings";
    public static final String ELE_configuration_ATT_typeAliases = "typeAliases";
    public static final String ELE_configuration_ATT_typeHandlers = "typeHandlers";
    public static final String ELE_configuration_ATT_objectFactory = "objectFactory";
    public static final String ELE_configuration_ATT_objectWrapperFactory = "objectWrapperFactory";
    public static final String ELE_configuration_ATT_reflectorFactory = "reflectorFactory";
    public static final String ELE_configuration_ATT_plugins = "plugins";
    public static final String ELE_configuration_ATT_environments = "environments";
    public static final String ELE_configuration_ATT_databaseIdProvider = "databaseIdProvider";
    public static final String ELE_configuration_ATT_mappers = "mappers";

    public static final List<String> ELE_configuration_attList = Arrays.asList(
            ELE_configuration_ATT_properties,
            ELE_configuration_ATT_settings,
            ELE_configuration_ATT_typeAliases,
            ELE_configuration_ATT_typeHandlers,
            ELE_configuration_ATT_objectFactory,
            ELE_configuration_ATT_objectWrapperFactory,
            ELE_configuration_ATT_reflectorFactory,
            ELE_configuration_ATT_plugins,
            ELE_configuration_ATT_environments,
            ELE_configuration_ATT_databaseIdProvider,
            ELE_configuration_ATT_mappers
    );

    public static final String ELE_settings_ATT_mappers = "mappers";

}
