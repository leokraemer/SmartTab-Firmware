// 
// Decompiled by Procyon v0.5.29
// 

package CommandHolders;

public class DriveCommand extends BaseCommandHolder
{
    private float speed;
    private float speedmax;
    private float speedmin;
    private float turn;
    private float turnmax;
    private float turnmin;
    private float vx;
    private float vxmax;
    private float vxmin;
    private float vy;
    private float vymax;
    private float vymin;
    
    public DriveCommand() {
        super(Command.DRIVE);
        this.speed = 0.0f;
        this.vx = 0.0f;
        this.vy = 0.0f;
        this.turn = 0.0f;
        this.vxmin = -1.0f;
        this.vxmax = 1.0f;
        this.vymin = -1.0f;
        this.vymax = 1.0f;
        this.turnmin = -1.0f;
        this.turnmax = 1.0f;
        this.speedmin = 0.0f;
        this.speedmax = 1000.0f;
    }
    
    public DriveCommand(final int n, final float vxmin, final float vymin, final float turnmin, final float speedmin, final float vxmax, final float vymax, final float turnmax, final float speedmax) {
        super(Command.DRIVE);
        this.speed = 0.0f;
        this.vx = 0.0f;
        this.vy = 0.0f;
        this.turn = 0.0f;
        this.vxmin = -1.0f;
        this.vxmax = 1.0f;
        this.vymin = -1.0f;
        this.vymax = 1.0f;
        this.turnmin = -1.0f;
        this.turnmax = 1.0f;
        this.speedmin = 0.0f;
        this.speedmax = 1000.0f;
        this.vxmin = vxmin;
        this.vymin = vymin;
        this.turnmin = turnmin;
        this.speedmin = speedmin;
        this.vxmax = vxmax;
        this.vymax = vymax;
        this.turnmax = turnmax;
        this.speedmax = speedmax;
    }
    
    public DriveCommand(final int n, final int n2) {
        super(Command.DRIVE);
        this.speed = 0.0f;
        this.vx = 0.0f;
        this.vy = 0.0f;
        this.turn = 0.0f;
        this.vxmin = -1.0f;
        this.vxmax = 1.0f;
        this.vymin = -1.0f;
        this.vymax = 1.0f;
        this.turnmin = -1.0f;
        this.turnmax = 1.0f;
        this.speedmin = 0.0f;
        this.speedmax = 1000.0f;
        this.setspeedmax(n2);
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public float getTurn() {
        return this.turn;
    }
    
    public float getTurnmax() {
        return this.turnmax;
    }
    
    public float getTurnmin() {
        return this.turnmin;
    }
    
    public float getVx() {
        return this.vx;
    }
    
    public float getVxmax() {
        return this.vxmax;
    }
    
    public float getVxmin() {
        return this.vxmin;
    }
    
    public float getVy() {
        return this.vy;
    }
    
    public float getVymax() {
        return this.vymax;
    }
    
    public float getVymin() {
        return this.vymin;
    }
    
    public float getspeedmax() {
        return this.speedmax;
    }
    
    public float getspeedmin() {
        return this.speedmin;
    }
    
    public void setSpeed(final float n) {
        this.speed = BaseCommandHolder.boundsCheck(n, this.speedmin, this.speedmax);
    }
    
    public void setTurn(final float n) {
        this.turn = BaseCommandHolder.boundsCheck(n, this.turnmin, this.turnmax);
    }
    
    public void setTurnmax(final float turnmax) {
        this.turnmax = turnmax;
    }
    
    public void setTurnmin(final float turnmin) {
        this.turnmin = turnmin;
    }
    
    public void setVx(final float n) {
        this.vx = BaseCommandHolder.boundsCheck(n, this.vxmin, this.vxmax);
    }
    
    public void setVxmax(final float vxmax) {
        this.vxmax = vxmax;
    }
    
    public void setVxmin(final float vxmin) {
        this.vxmin = vxmin;
    }
    
    public void setVy(final float n) {
        this.vy = BaseCommandHolder.boundsCheck(n, this.vymin, this.vymax);
    }
    
    public void setVymax(final float vymax) {
        this.vymax = vymax;
    }
    
    public void setVymin(final float vymin) {
        this.vymin = vymin;
    }
    
    public void setspeedmax(final float speedmax) {
        this.speedmax = speedmax;
    }
    
    public void setspeedmin(final float speedmin) {
        this.speedmin = speedmin;
    }
    
    public DriveCommand update(final float speed, final float vx, final float vy, final float turn) {
        this.setSpeed(speed);
        this.setVx(vx);
        this.setVy(vy);
        this.setTurn(turn);
        return this;
    }
}
