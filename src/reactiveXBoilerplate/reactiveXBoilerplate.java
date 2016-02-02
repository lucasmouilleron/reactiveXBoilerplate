package reactiveXBoilerplate;

import rx.Observable;
import rx.Subscriber;

public class reactiveXBoilerplate
{

    ////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args)
    {
        Observable<String> syncSimpleObservable = Observable.just("a", "b", "c");
        syncSimpleObservable.subscribe(new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("Finised simple");
            }

            @Override
            public void onError(Throwable throwable)
            {

            }

            @Override
            public void onNext(String s)
            {
                System.out.println("Recieved : " + s);
            }
        });

        Observable<String> asyncObservable = Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                new expensiveThread(subscriber).start();
            }
        });
        asyncObservable.subscribe(new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("Finished custom");
            }

            @Override
            public void onError(Throwable throwable)
            {

            }

            @Override
            public void onNext(String s)
            {
                System.out.println("Recieved : " + s);
            }
        });
        System.out.println("Main over !");
    }

    ////////////////////////////////////////////////////////////////////////////////
    private static class expensiveThread extends Thread
    {
        public Subscriber<? super String> subscriber;

        public expensiveThread(Subscriber<? super String> subscriber)
        {
            this.subscriber = subscriber;
        }

        @Override
        public void run()
        {
            System.out.println("Doing something expensive");
            reactiveXBoilerplate.sleep(3000);
            subscriber.onNext("Expensive result");
            subscriber.onCompleted();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    public static void sleep(long sleepInMSecs)
    {
        try
        {
            Thread.sleep(sleepInMSecs);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
