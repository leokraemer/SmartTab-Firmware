// 
// Decompiled by Procyon v0.5.29
// 

package CommandHolders;

public class LiftingArmCommand extends BaseCommandHolder
{
    private float position;
    private float positionmax;
    private float positionmin;
    private int power;
    private int powerMax;
    private int powerMin;
    
    public LiftingArmCommand() {
        super(Command.LIFTINGARM);
        this.position = 0.0f;
        this.positionmin = 0.0f;
        this.positionmax = 100.0f;
        this.power = 0;
        this.powerMin = 0;
        this.powerMax = 100;
    }
    
    public float getPosition() {
        return this.position;
    }
    
    public float getPositionmax() {
        return this.positionmax;
    }
    
    public float getPositionmin() {
        return this.positionmin;
    }
    
    public int getPower() {
        return this.power;
    }
    
    public int getPowerMax() {
        return this.powerMax;
    }
    
    public int getPowerMin() {
        return this.powerMin;
    }
    
    public void setPosition(final float n) {
        this.position = BaseCommandHolder.boundsCheck(n, this.positionmin, this.positionmax);
    }
    
    public void setPositionmax(final int n) {
        this.positionmax = n;
    }
    
    public void setPositionmin(final int n) {
        this.positionmin = n;
    }
    
    public void setPower(final int n) {
        this.power = BaseCommandHolder.boundsCheck(n, this.powerMin, this.powerMax);
    }
    
    public LiftingArmCommand update(final float position, final int power) {
        this.setPosition(position);
        this.setPower(power);
        return this;
    }
}
