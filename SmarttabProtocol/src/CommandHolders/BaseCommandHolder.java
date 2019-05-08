// 
// Decompiled by Procyon v0.5.29
// 

package CommandHolders;

public class BaseCommandHolder implements ICommandHolder
{
    private final Command command;
    
    public BaseCommandHolder(final Command command) {
        this.command = command;
    }
    
    public static float boundsCheck(final float n, final float n2, final float n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    public static int boundsCheck(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    @Override
    public Command getCommand() {
        return this.command;
    }
}
