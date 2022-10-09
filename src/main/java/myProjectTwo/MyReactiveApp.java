package myProjectTwo;


import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.Random;

public class MyReactiveApp {

    public static void main(String args[]) throws InterruptedException {


        Flux<Object> producer = Flux
                .generate(sink -> {
                    sink.next(createString());
                });


        Flux.create(sink -> producer
                .subscribe(new BaseSubscriber<Object>() {
                            @Override
                            protected void hookOnNext(Object value) {
                                sink.next(value);
                            }
                            @Override
                            protected void hookOnComplete() {
                                sink.complete();
                            }
                        }))
                .subscribe(System.out::println);
    }
    public static String createString(){
        String str =  new Random()
                .ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(1 + (int) ( Math.random() * 30))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        if (str.length() < 21) return str;
        return "There are more than 20 characters in a line";
    }
}