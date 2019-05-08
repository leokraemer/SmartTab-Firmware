import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import CommandHolders.DisconnectCommand;
import CommandHolders.DriveCommand;
import CommandHolders.GetStausCommand;
import CommandHolders.ICommandHolder;
import CommandHolders.ICommandHolder.Command;
import CommandHolders.LiftingArmCommand;
import ProtocolWriter.ProtocolReaderWriter;
import ResponseHolders.IResponseHolder.ResponseCode;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TachoMotorPort;
import lejos.nxt.addon.LnrActrFirgelliNXT;
import lejos.nxt.addon.MMXRegulatedMotor;
import lejos.nxt.addon.NXTMMX;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class SmartBoy {

	protected RegulatedMotor motor1;
	protected RegulatedMotor motor2;
	protected RegulatedMotor motor3;
	protected RegulatedMotor motor4;
	protected LnrActrFirgelliNXT linearActuator;

	protected void setMaxSpeed() {
		MAX_SPEED = Math.min(Math.min(motor1.getMaxSpeed(), motor2.getMaxSpeed()),
				Math.min(motor3.getMaxSpeed(), motor4.getMaxSpeed()));
	}

	/**
	 * Utility method to get Motor instance from string (A, B or C)
	 */
	public static RegulatedMotor getMotor(String motor) {
		NXTMMX mux = new NXTMMX(SensorPort.S1);
		if (motor.equals("A"))
			return Motor.A;
		else if (motor.equals("B"))
			return Motor.B;
		else if (motor.equals("C"))
			return Motor.C;
		else if (motor.equals("1"))
			return new MMXRegulatedMotor(mux, NXTMMX.MMX_MOTOR_1);
		else if (motor.equals("2"))
			return new MMXRegulatedMotor(mux, NXTMMX.MMX_MOTOR_2);
		else
			return null;
	}

	public static LnrActrFirgelliNXT getLinAcc() {
		return new LnrActrFirgelliNXT(MotorPort.C);
	}

	protected ResponseCode status = ResponseCode.OK;

	protected float MAX_SPEED;

	protected void stopAllMotors() {
		motor1.stop(true);
		motor2.stop(true);
		motor3.stop(true);
		motor4.stop(true);
	}

	private void startMotor(RegulatedMotor motor, double motorSpeed) {
		motor.setSpeed((int) Math.round(MAX_SPEED * motorSpeed));
		if (motorSpeed > 0) {
			motor.forward();
		} else {
			motor.backward();
		}
	}

	/**
	 * Estabish bluetooth connection to mission control
	 */
	public void connect() {
		LCD.clear();
		LCD.drawString("Waiting", 0, 0);
		connection = Bluetooth.waitForConnection(); // this method is very
													// patient.
		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		dataIn = connection.openDataInputStream();
		dataOut = connection.openDataOutputStream();
		Sound.beepSequence();
	}

	/**
	 * connect and wait for orders
	 * 
	 * @throws InterruptedException
	 */
	protected void go() throws InterruptedException {
		connect();
		readThread.start();
		sendThread.start();
		stallDetector.start();
		while (true) {
			if (Button.ENTER.isDown()) {
				readThread.interrupt();
				sendThread.interrupt();
				readThread.join();
				sendThread.join();
				try {
					dataIn.close();
					dataOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					this.wait(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				connection.close();
				stopAllMotors();
				System.exit(0);
			}
		}
	}

	protected float tempv1;
	protected float tempv2;
	protected float tempv3;
	protected float tempv4;
	protected float overflow = 0;

	private DriveCommand driveCommand = new DriveCommand();
	private LiftingArmCommand liftingArmCommand = new LiftingArmCommand();
	private GetStausCommand getStausCommand = new GetStausCommand();
	private DisconnectCommand disconnectCommand = new DisconnectCommand();

	protected Thread readThread = new Thread() {

		@Override
		public void run() {
			while (!isInterrupted())
				readData();
		}

		/**
		 * decode incoming messages and handle motors accordingly
		 * 
		 * {int: command, ...}
		 * 
		 * {GETSTATUS = 1}
		 * 
		 * 
		 * {DRIVE = 2, float: topSpeed in [0, Float.MaxValue], float: vx in
		 * [-1,1], float: vy in [-1,1], float: turn in [-1,1]}
		 * 
		 * 
		 * {LIFTINGARM = 3, float: position in [0,90]}
		 * 
		 * 
		 * {DISCONNECT = 4}
		 * 
		 * 
		 * response: {int: status, int: actualSpeed}
		 * 
		 * status: 0: ok; 1: skip invalid command; -1: error;
		 * 
		 * actualSpeed in [0, infinity]
		 * 
		 * 
		 * Wheels: Front ---1--- | | | 2- - -3 | | | ---4--- Back see:
		 * http://www.firstroboticscanada.org/main/wp-content/uploads/
		 * Omnidirectional-Drive-Systems.pdf
		 */
		protected void readData() {
			try {
				if (dataIn.available() > 0) {
					ICommandHolder command = ProtocolReaderWriter.readCommandFromDataIn(driveCommand, getStausCommand,
							liftingArmCommand, disconnectCommand, dataIn);
					switch (command.getCommand()) {
					case DRIVE:
						handleDriveCommand((DriveCommand) command);
						break;
					case GETSTATUS:
						sendStatus();
						break;
					case LIFTINGARM:
						handleLiftingarmCommand((LiftingArmCommand) command);
						break;
					case DISCONNECT:
						disconnect();
						break;
					case RESETLIFTINGARM:
						resetLiftingarm();

					}
				} else {
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.out.println("Read exception " + e);
			}
		}

		private void disconnect() {
			status = ResponseCode.DISCONNECT;
			send = true;
			connection.close();
			System.exit(0);
		}

		private void handleLiftingarmCommand(LiftingArmCommand command) throws IOException {

			driveLiftingarmToAngle(command.getPosition(), command.getPower());
			status = ResponseCode.OK;
			send = true;
		}

		private void sendStatus() {
			send = true;
		}

		private boolean debug = true;

		private void handleDriveCommand(DriveCommand driveCommand) throws IOException {
			if (debug) {
				LCD.clear();
				LCD.drawString(Float.toString(driveCommand.getSpeed()), 0, 3);
				LCD.drawString(Float.toString(driveCommand.getVx()), 0, 4);
				LCD.drawString(Float.toString(driveCommand.getVy()), 0, 5);
				LCD.drawString(Float.toString(driveCommand.getTurn()), 0, 6);
				LCD.drawString(Float.toString(MAX_SPEED), 0, 7);
			}
			status = ResponseCode.OK;
			send = true;
			if ((driveCommand.getVx() != 0 || driveCommand.getVy() != 0 || driveCommand.getTurn() != 0)
					&& driveCommand.getSpeed() > 0) {
				setMotorSpeed(driveCommand.getVx(), driveCommand.getVy(), driveCommand.getTurn());
				overflow = Math.max(Math.max(Math.abs(tempv1), Math.abs(tempv2)),
						Math.max(Math.abs(tempv3), Math.abs(tempv4)));
				if (overflow > 1) {
					tempv1 /= overflow;
					tempv2 /= overflow;
					tempv3 /= overflow;
					tempv4 /= overflow;
				}
				startMotor(motor1, tempv1);
				startMotor(motor2, tempv2);
				startMotor(motor3, tempv3);
				startMotor(motor4, tempv4);
			} else {
				stopAllMotors();
			}
		}

	};
	protected boolean send = false;

	protected Thread sendThread = new Thread() {

		@Override
		public void run() {
			while (!isInterrupted())
				sendData();
		}

		protected void sendData() {
			if (send) {
				send = false;
				try {
					dataOut.writeInt(status.ordinal());
					dataOut.writeInt(Math.round(Math.min(MAX_SPEED, driveCommand.getSpeed())));
					dataOut.flush();
				} catch (IOException e) {
					LCD.clear();
					LCD.drawString("ERROR Sending to host", 0, 3);
				}
			} else {
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	protected boolean running = true;

	protected boolean stalled = false;

	protected Thread stallDetector = new Thread() {
		public void run() {
			while (running) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (motor1.isStalled()) {
					LCD.drawString("Motor 1 stalled", 0, 0);
					startMotor(motor1, tempv1);
				}
				if (motor2.isStalled()) {
					LCD.drawString("Motor 2 stalled", 1, 0);
					startMotor(motor2, tempv2);
				}
				if (motor3.isStalled()) {
					LCD.drawString("Motor 3 stalled", 2, 0);
					startMotor(motor3, tempv3);
				}
				if (motor4.isStalled()) {
					LCD.drawString("Motor 4 stalled", 3, 0);
					startMotor(motor4, tempv4);
				}
			}
		}
	};

	private boolean liftingarmIsResetting = false;

	private void resetLiftingarm() {
		liftingarmIsResetting = true;
		liftingarmResetThread.start();
	}

	private Thread liftingarmResetThread = new Thread() {
		public void run() {
			linearActuator.setPower(25);
			linearActuator.move(-1000, true);
			while (liftingarmIsResetting) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// nothing
				}
			}
			if (liftingarmIsResetting)
				stopLiftingarmResetting();
		}
	};

	private void stopLiftingarmResetting() {
		liftingarmIsResetting = false;
		linearActuator.resetTachoCount();
	}

	public static void main(String[] args) throws InterruptedException {
		new SmartBoy().go();
	}

	public SmartBoy() {
		motor1 = getMotor("A");
		motor2 = getMotor("B");
		motor3 = getMotor("1");
		motor4 = getMotor("2");
		linearActuator = getLinAcc();
		setMaxSpeed();
	}

	/**
	 * decode incoming messages and handle motors accordingly
	 * 
	 * Wheels:
	 * 
	 * A---B ||| ||| 1---2
	 * 
	 * see: http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf
	 */
	protected void setMotorSpeed(float vx, float vy, float turningspeed) {
		tempv1 = vy + vx + turningspeed;
		tempv2 = vy - vx - turningspeed;
		tempv3 = -vy + vx - turningspeed;
		tempv4 = -vy - vx + turningspeed;
	}

	// 0.5 mm/encoder tick
	// total length = 50mm -> 100 Ticks
	protected void driveLiftingarmToAngle(float position, int power) {
		if (liftingarmIsResetting)
			stopLiftingarmResetting();
		linearActuator.setPower(power);
		linearActuator.moveTo(Math.round(position), true);
	}

	protected BTConnection connection;
	protected DataInputStream dataIn;
	protected DataOutputStream dataOut;
}