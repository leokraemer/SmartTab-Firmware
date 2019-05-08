// 
// Decompiled by Procyon v0.5.29
// 

package ResponseHolders;

public class StatusResponse extends BaseResponseHolder
{
    private float auxBatteryVoltage;
    private float batteryVoltage;
    private boolean liftingarmresetting;
    private int linactTachoCount;
    private boolean motor1stalled;
    private boolean motor2stalled;
    private boolean motor3stalled;
    private boolean motor4stalled;
    private float speed;
    
    public StatusResponse(final ResponseCode responseCode) {
        super(responseCode);
        this.speed = 0.0f;
        this.motor1stalled = false;
        this.motor2stalled = false;
        this.motor3stalled = false;
        this.motor4stalled = false;
        this.liftingarmresetting = false;
        this.linactTachoCount = 0;
        this.batteryVoltage = 0.0f;
        this.auxBatteryVoltage = 0.0f;
    }
    
    public StatusResponse(final ResponseCode responseCode, final float speed, final boolean motor1stalled, final boolean motor2stalled, final boolean motor3stalled, final boolean motor4stalled, final boolean liftingarmresetting, final int linactTachoCount, final float batteryVoltage, final float auxBatteryVoltage) {
        super(responseCode);
        this.speed = 0.0f;
        this.motor1stalled = false;
        this.motor2stalled = false;
        this.motor3stalled = false;
        this.motor4stalled = false;
        this.liftingarmresetting = false;
        this.linactTachoCount = 0;
        this.batteryVoltage = 0.0f;
        this.auxBatteryVoltage = 0.0f;
        this.speed = speed;
        this.auxBatteryVoltage = auxBatteryVoltage;
        this.batteryVoltage = batteryVoltage;
        this.liftingarmresetting = liftingarmresetting;
        this.motor1stalled = motor1stalled;
        this.motor2stalled = motor2stalled;
        this.motor3stalled = motor3stalled;
        this.motor4stalled = motor4stalled;
        this.linactTachoCount = linactTachoCount;
    }
    
    public float getAuxBatteryVoltage() {
        return this.auxBatteryVoltage;
    }
    
    public float getBatteryVoltage() {
        return this.batteryVoltage;
    }
    
    public int getLinactTachoCount() {
        return this.linactTachoCount;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public boolean isLiftingarmresetting() {
        return this.liftingarmresetting;
    }
    
    public boolean isMotor1stalled() {
        return this.motor1stalled;
    }
    
    public boolean isMotor2stalled() {
        return this.motor2stalled;
    }
    
    public boolean isMotor3stalled() {
        return this.motor3stalled;
    }
    
    public boolean isMotor4stalled() {
        return this.motor4stalled;
    }
}
