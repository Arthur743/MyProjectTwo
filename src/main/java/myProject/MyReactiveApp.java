package myProject;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Stream;

public class MyReactiveApp {

    public static void main(String args[]) {

        try(SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {


            publisher.subscribe(new Subscriber<String>() {
                private Subscription subscription;

                @Override
                public void onSubscribe(Subscription subscription) {
                    System.out.println(this.getClass().getName() +"[START]");

                    this.subscription = subscription;
                    this.subscription.request(1);

                }

                @Override
                public void onNext(String str) {
                    if (str.length() < 21) {
                        System.out.println(this.getClass().getName() + '['+str+']');
                    } else {
                        System.out.println("There are more than 20 characters in a line");
                    }
                    this.subscription.request(1);
                }

                @Override
                public void onError(Throwable ex) {
                    System.out.println(this.getClass().getName() +"[ERROR]");
                    ex.printStackTrace();
                }

                @Override
                public void onComplete() {
                    System.out.println(this.getClass().getName() +"[THE END]");
                }
            });


            Stream.generate(() -> createString()).peek(x -> publisher.submit(x)).forEach(System.out::println);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String createString(){
        return new Random().ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(1 + (int) ( Math.random() * 30))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}