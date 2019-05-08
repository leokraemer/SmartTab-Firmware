// 
// Decompiled by Procyon v0.5.29
// 

package ResponseHolders;

public interface IResponseHolder
{
    ResponseCode getCode();
    
    public enum ResponseCode
    {
        DISCONNECT("DISCONNECT", 3), 
        ERROR("ERROR", 1), 
        LIFTINGARMISRESETTING("LIFTINGARMISRESETTING", 6), 
        LOWBATTERY("LOWBATTERY", 4), 
        MOTORSTALLED("MOTORSTALLED", 5), 
        OK("OK", 0), 
        SKIPINVALIDCOMMAND("SKIPINVALIDCOMMAND", 2);
        
        private ResponseCode(String s, int n) {
        }
    }
}
