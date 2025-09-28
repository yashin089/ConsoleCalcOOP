import java.util.Scanner;

//Комментарий для коммита 2
public class ConsoleCalcOOP {

    //Объявляем тут, т.к. будут дампы при повторном создании и считывании с System.in
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Calculator calc = new Calculator(scanner);
        calc.run();
        scanner.close();

    }

}

class Calculator {

    private final Scanner scanner;

    //Первый операнд
    private double value1;

    //Второй операнд
    private double value2;

    //Операция
    private OperEnum operEnum;

    public Calculator(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {

        boolean isNoShowInf = false;    //Флаг, чтобы не показывать сообщене о запросе значения
        boolean isExit = false;     //Флаг для команды Выход

        information();

        do {

            //Запрашиваем выражение от пользователя
            if (getExpression(isNoShowInf)) {

                if (operEnum == OperEnum.EXIT) {
                    isExit = true;
                } else {
                    //Выполняем математическую операцию заданную пользователем
                    double result2 = getOperation(value1, value2, operEnum).calculate();
                    System.out.println("Результат: " + result2);
                    isNoShowInf = false;
                }

            } else {    //Если не удалось получить корректное выражение

                System.out.println("Неверное выражение. Введите еще раз:");
                isNoShowInf = true;

            }

        } while (!isExit); //Выполняем цикл пока не получим команду exit

    }

    private void information() {

        System.out.println("Консольный калькулятор.");
        System.out.println("Для получения результата введите выражение.");
        System.out.println("Например: 1,5 + 28,5");
        System.out.println("Доступные операции:");
        System.out.println("Сложение ( + )");
        System.out.println("Вычитание ( - )");
        System.out.println("Умножение ( * )");
        System.out.println("Деление ( / )");
        System.out.println("Целочисленное деления ( // )");
        System.out.println("Возведения в степень ( ^ )");
        System.out.println("Остаток от деления ( % )");
        System.out.println("Для выхода из программы введите команду ( exit )");

    }

    //Получение выражения от пользователя
    private boolean getExpression(boolean isNoShowInf) {

        String operString;

        if (!isNoShowInf) {
            System.out.println("Введите выражение:");
        }

        //Запрашиваем выражение от пользователя
        String expression = scanner.nextLine();

        if (expression.equals("exit")) {

            //Если получена команда exit, то запоминаем ее и выходим
            operEnum = OperEnum.EXIT;
            return true;

        } else {

            //Создаем сканер по полученному выражению
            Scanner scannerExpr = new Scanner(expression);
            scannerExpr.useDelimiter(" "); // разделяем выражение по пробелу на отдельные части

            //Проверяем является ли следующее значение числом
            if (scannerExpr.hasNextDouble()) {

                //Задаем значение для первого операнда
                value1 = scannerExpr.nextDouble();

            } else {

                //Закрываем сканер по выражению и возвращаем ошибку
                scannerExpr.close();
                return false;

            }

            //Проверяем есть ли следующее значение
            if (scannerExpr.hasNext()) {

                //Задаем значение для операции
                operString = scannerExpr.next();
                operEnum = getOperEnum(operString);

            } else {

                //Закрываем сканер по выражению и возвращаем ошибку
                scannerExpr.close();
                return false;

            }

            //Проверяем является ли следующее значение числом
            if (scannerExpr.hasNextDouble()) {

                //Задаем значение для второго операнда
                value2 = scannerExpr.nextDouble();

            } else {

                //Закрываем сканер по выражению и возвращаем ошибку
                scannerExpr.close();
                return false;

            }

            //Закрываем сканер по выражению
            scannerExpr.close();

        }

        //Проверяем полученное выражение
        return checkExpression();

    }

    //Проверка корректности выражения
    private boolean checkExpression() {

        //Проверяем на ввод доступных операций
        if (operEnum != OperEnum.ADDITION &&
                operEnum != OperEnum.SUBTRACT &&
                operEnum != OperEnum.MULTIPLY &&
                operEnum != OperEnum.DIVIDE) {

            return false;

        }

        //Проверяем деление на 0
        if (operEnum == OperEnum.DIVIDE && value2 == 0) {

            System.out.println("Ошибка - деление на 0.");
            return false;

        }

        return true;

    }

    private Operation getOperation(double value1, double value2, OperEnum operEnum) {

        return switch (operEnum) {
            case ADDITION:
                yield new Addition(value1, value2);
            case SUBTRACT:
                yield new Subtract(value1, value2);
            case MULTIPLY:
                yield new Multiply(value1, value2);
            case DIVIDE:
                yield new Divide(value1, value2);
            default:
                yield null;
        };

    }

    private OperEnum getOperEnum(String operString) {

        return switch (operString) {
            case "+":
                yield OperEnum.ADDITION;
            case "-":
                yield OperEnum.SUBTRACT;
            case "*":
                yield OperEnum.MULTIPLY;
            case "/":
                yield OperEnum.DIVIDE;
            case "exit":
                yield OperEnum.EXIT;
            default:
                yield null;

        };

    }

}

interface iOperation {
    double calculate();
}

abstract class Operation implements iOperation {
    //Первый операнд
    private final double value1;

    //Второй операнд
    private final double value2;

    public Operation(double value1, double value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public double getValue1() {
        return value1;
    }

    public double getValue2() {
        return value2;
    }

}

class Addition extends Operation {

    public Addition(double value1, double value2) {
        super(value1, value2);
    }

    @Override
    public double calculate() {
        return getValue1() + getValue2();
    }
}

class Subtract extends Operation {

    public Subtract(double value1, double value2) {
        super(value1, value2);
    }

    @Override
    public double calculate() {
        return getValue1() - getValue2();
    }
}

class Multiply extends Operation {

    public Multiply(double value1, double value2) {
        super(value1, value2);
    }

    @Override
    public double calculate() {
        return getValue1() * getValue2();
    }
}

class Divide extends Operation {

    public Divide(double value1, double value2) {
        super(value1, value2);
    }

    @Override
    public double calculate() {
        return getValue1() / getValue2();
    }
}

enum OperEnum {
    ADDITION,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    EXIT
}

//Комментарий для коммита 2