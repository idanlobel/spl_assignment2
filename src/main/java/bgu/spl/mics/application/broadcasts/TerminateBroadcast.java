package bgu.spl.mics.application.broadcasts;

import bgu.spl.mics.Broadcast;

public class TerminateBroadcast implements Broadcast {
    boolean terminate;
    public TerminateBroadcast(){
        terminate=false;
    }

    public void Terminate() {
        this.terminate = true;
    }
}
