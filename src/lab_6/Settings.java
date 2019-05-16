package lab_6;

/**
 * Класс с глобальными настройками сервера и клиента.
 */
public final class Settings {
    /**
     * Стандартный порт сервера
     */
    public final static int ApplicationPort = 25768;
    /**
     * Минимально допустимая длина логина
     */
    public final static int loginMinimalLength = 3;
    /**
     * Максимально допустимая длина логина
     */
    public final static int loginMaximalLength = 20;
    /**
     * Длина случайной последовательности для подтверждения личности пользователя
     */
    public final static int identificationRandomSize= 200;
    /**
     * Длина (в байтах) хеша на основе пароля пользователя, являющегося AES секретным ключом.
     */
    public final static int userAESKeySize = 32;
    /**
     * Длина (в битах) RSA ключей для регистрации пользователя.
     */
    public final static int userRSAKeyLength = 2048;
    /**
     * Время ожидания сообщения на стороне клиента. (в секундах)
     */
    public final static int clientReceiveTimeout = 10;
    /**
     * Размер буфера входного канала клиента. (в байтах)
     */
    public final static int clientReceiveBuffer = 10000000;
    /**
     * Количество потоков, выделяемых на сервере для обработки запросов пользователей.
     */
    public final static int threadPoolSize = 4;
    /**
     * Путь к базе данных.
     */
    public final static String databasePath = "src/database/";
}
