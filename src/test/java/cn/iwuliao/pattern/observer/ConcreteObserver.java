package cn.iwuliao.pattern.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @author tangyu
 * @since 2019-04-08 18:29
 */
public class ConcreteObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {

        System.out.println("i am here");

    }
}
