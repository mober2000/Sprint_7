import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    //Генерирует случайный логин из 10-ти символов
    public String randomLogin() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

}