// 
// Decompiled by Procyon v0.5.29
// 

package ResponseHolders;

public class BaseResponseHolder implements IResponseHolder
{
    private final ResponseCode code;
    
    public BaseResponseHolder(final ResponseCode code) {
        this.code = code;
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
    public ResponseCode getCode() {
        return this.code;
    }
}
