package ProtocolWriter;

import CommandHolders.DisconnectCommand;
import CommandHolders.DriveCommand;
import CommandHolders.GetStausCommand;
import CommandHolders.ICommandHolder;
import CommandHolders.InvalidCommandCode;
import CommandHolders.LiftingArmCommand;
import CommandHolders.ResetLiftingArmCommand;
import ResponseHolders.IResponseHolder;
import ResponseHolders.StatusResponse;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ProtocolReaderWriter {
	public static ICommandHolder readCommandFromDataIn(DriveCommand driveCommand, GetStausCommand getStausCommand,
			LiftingArmCommand liftingArmCommand, DisconnectCommand disconnectCommand, DataInputStream dataInputStream)
					throws IOException {
		ICommandHolder.Command localCommand = ICommandHolder.Command.values()[dataInputStream.readInt()];
		switch (localCommand) {
		case GETSTATUS:
			return getStausCommand;
		case DRIVE:
			return driveCommand.update(dataInputStream.readFloat(), dataInputStream.readFloat(),
					dataInputStream.readFloat(), dataInputStream.readFloat());
		case LIFTINGARM:
			return liftingArmCommand.update(dataInputStream.readFloat(), dataInputStream.readInt());
		case DISCONNECT:
			return disconnectCommand;
		case RESETLIFTINGARM:
			return new ResetLiftingArmCommand();
		default:
			return new InvalidCommandCode();
		}
	}

	public static StatusResponse readResponseFromDataIn(DataInputStream dataInputStream) throws IOException {
		return new StatusResponse(IResponseHolder.ResponseCode.values()[dataInputStream.readInt()],
				dataInputStream.readFloat(), dataInputStream.readBoolean(), dataInputStream.readBoolean(),
				dataInputStream.readBoolean(), dataInputStream.readBoolean(), dataInputStream.readBoolean(),
				dataInputStream.readInt(), dataInputStream.readFloat(), dataInputStream.readFloat());
	}

	public static void writeCommandToDataoutAndFlush(ICommandHolder iCommandHolder, DataOutputStream dataOutputStream)
			throws IOException {
		switch (iCommandHolder.getCommand()) {
		case INVALID:
		case GETSTATUS:
		case DISCONNECT:
		default:
			dataOutputStream.writeInt(iCommandHolder.getCommand().ordinal());
			dataOutputStream.flush();
			return;
		case DRIVE:
			dataOutputStream.writeInt(iCommandHolder.getCommand().ordinal());
			dataOutputStream.writeFloat(((DriveCommand) iCommandHolder).getSpeed());
			dataOutputStream.writeFloat(((DriveCommand) iCommandHolder).getVx());
			dataOutputStream.writeFloat(((DriveCommand) iCommandHolder).getVy());
			dataOutputStream.writeFloat(((DriveCommand) iCommandHolder).getTurn());
			dataOutputStream.flush();
			return;

		case LIFTINGARM:
			dataOutputStream.writeInt(iCommandHolder.getCommand().ordinal());
			dataOutputStream.writeFloat(((LiftingArmCommand) iCommandHolder).getPosition());
			dataOutputStream.writeInt(((LiftingArmCommand) iCommandHolder).getPower());
			dataOutputStream.flush();
			return;
		}
	}

	public static void writeStatusResponseToDataoutAndFlush(StatusResponse statusResponse,
			DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(statusResponse.getCode().ordinal());
		dataOutputStream.writeFloat(statusResponse.getSpeed());
		dataOutputStream.writeBoolean(statusResponse.isMotor1stalled());
		dataOutputStream.writeBoolean(statusResponse.isMotor2stalled());
		dataOutputStream.writeBoolean(statusResponse.isMotor3stalled());
		dataOutputStream.writeBoolean(statusResponse.isMotor4stalled());
		dataOutputStream.writeBoolean(statusResponse.isLiftingarmresetting());
		dataOutputStream.writeInt(statusResponse.getLinactTachoCount());
		dataOutputStream.writeFloat(statusResponse.getBatteryVoltage());
		dataOutputStream.writeFloat(statusResponse.getAuxBatteryVoltage());
		dataOutputStream.flush();
	}
}

/*
 * Location:
 * C:\Users\Leo\Desktop\dex2jar-2.0\classes-dex2jar.jar!\ProtocolWriter\
 * ProtocolReaderWriter.class Java compiler version: 6 (50.0) JD-Core Version:
 * 0.7.1
 */