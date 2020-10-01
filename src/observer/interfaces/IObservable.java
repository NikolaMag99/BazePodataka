package observer.interfaces;

public interface IObservable {
	void addObserver(IOListener listener);
	void removeObserver(IOListener listener);
	void notifyObserver(Object event);
}
