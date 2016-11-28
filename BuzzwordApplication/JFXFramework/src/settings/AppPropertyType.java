package settings;

/**
 * This enum provides properties that are to be loaded via
 * XML files to be used for setting up the application.
 *
 * @author Richard McKenna, Ritwik Banerjee
 * @author Steven Li
 * @version 1.0
 */
@SuppressWarnings("unused")
public enum AppPropertyType {

    // from app-properties.xml
    APP_WINDOW_WIDTH,
    APP_WINDOW_HEIGHT,
    APP_TITLE,
    APP_LOGO,
    APP_CSS,
    APP_PATH_CSS,

    // ERROR MESSAGES
    LOAD_ERROR_MESSAGE,
    NEW_ERROR_MESSAGE,
    SAVE_ERROR_MESSAGE,
    PROPERTIES_LOAD_ERROR_MESSAGE,

    // ERROR TITLES
    LOAD_ERROR_TITLE,
    NEW_ERROR_TITLE,
    SAVE_ERROR_TITLE,
    PROPERTIES_LOAD_ERROR_TITLE,
    EMPTY_LOGIN_ERROR,
    INVALID_LOGIN_ERROR,
    LOGIN_ERROR_TITLE,
    LOGIN_ERROR,

    // AND VERIFICATION MESSAGES AND TITLES
    NEW_COMPLETED_MESSAGE,
    NEW_COMPLETED_TITLE,
    SAVE_COMPLETED_MESSAGE,
    SAVE_COMPLETED_TITLE,
    SAVE_UNSAVED_WORK_TITLE,
    SAVE_UNSAVED_WORK_MESSAGE,
    LOAD_COMPLETED_MESSAGE,
    LOAD_COMPLETED_TITLE,
    SURE_CLOSE_TITLE,
    SURE_CLOSE_MESSAGE,

    SAVE_WORK_TITLE,
    WORK_FILE_EXT,
    WORK_FILE_EXT_DESC,
    PROPERTIES_
}
