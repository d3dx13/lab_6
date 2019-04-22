package lab_6.server.serverTesting;

import lab_6.message.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MultiObject {
    public static void main(String[] args) throws IOException, InterruptedException {

        // запустим пул нитей в которых колличество возможных нитей ограничено -
        // 10-ю.
        ExecutorService exec = Executors.newFixedThreadPool(3);
        int j = 0;

        // стартуем цикл в котором с паузой в 10 милисекунд стартуем Runnable
        // клиентов,
        // которые пишут какое-то количество сообщений
        while (j < 10) {
            j++;
            Message msg = new Message();
            msg.login = "d3dx13"+j;
            msg.text = "d3dx13"+j*j;
            msg.time = 1L*j*j*j*j*j*j*j*j*j*j*j*j;
            exec.execute(new TestRunnableClientTester(msg));
        }

        // закрываем фабрику
        exec.shutdown();
    }
}
