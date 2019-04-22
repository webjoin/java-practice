package cn.iwuliao.pattern.observer;

import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

/**
 * @author tangyu
 * @since 2019-04-08 18:14
 */
public class ObservableTest {


    @Test
    public void ba() {
        Observer observer1 = new ConcreteObserver();

        Observable observable = new Observable();
        observable.addObserver(observer1);

//        observable.set
        observable.notifyObservers();

        System.out.println("11111");

    }

}
