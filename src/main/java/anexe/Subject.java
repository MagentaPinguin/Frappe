package anexe;

import anexe.Observer;

public interface Subject {

    //methods to register and unregister observers
    public void register(Observer obj);

    //method to notify observers of change
    public void notifyObservers();

}