package uk.co.electronstudio.snakeeaters;

import org.robovm.rt.GC;
import uk.co.electronstudio.retrowar.ManualGC;

public class IOSManualGC implements ManualGC {
    @Override
    public void enable() {
        GC.enable();
    }

    @Override
    public void disable() {
        GC.disable();
    }

    @Override
    public void doGC() {
        System.gc();
    }
}
