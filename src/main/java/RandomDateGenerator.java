import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RandomDateGenerator {

    public static void main(String[] args) {
        Date randomDate = generateRandomDate();
        System.out.println("Random Date: " + randomDate);
    }

    public static Date generateRandomDate() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();

        // Генерируем случайное количество дней от начала 1900 года до текущей даты
        int randomDays = random.nextInt((int) (System.currentTimeMillis() / (1000*60*60*24)));

        // Устанавливаем случайную дату
        calendar.add(Calendar.DAY_OF_YEAR, -randomDays);
        return calendar.getTime();
    }
}

