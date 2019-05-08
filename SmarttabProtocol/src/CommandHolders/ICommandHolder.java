// 
// Decompiled by Procyon v0.5.29
// 

package CommandHolders;

public interface ICommandHolder
{
    Command getCommand();
    
    public enum Command
    {
        DISCONNECT("DISCONNECT", 3), 
        DRIVE("DRIVE", 1), 
        GETSTATUS("GETSTATUS", 0), 
        INVALID("INVALID", 4), 
        LIFTINGARM("LIFTINGARM", 2), 
        RESETLIFTINGARM("RESETLIFTINGARM", 5);
        
        private Command(String s, int n) {
        }
    }
}
