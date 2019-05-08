package test;

import CommandHolders.DisconnectCommand;
import CommandHolders.DriveCommand;
import CommandHolders.GetStausCommand;
import CommandHolders.ICommandHolder;
import CommandHolders.LiftingArmCommand;
import ProtocolWriter.ProtocolReaderWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestProtocolReader
{
  DataInputStream dataIn;
  
  public TestProtocolReader(byte[] paramArrayOfByte)
  {
    dataIn = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
  }
  
  public InputStream getDataIn()
  {
    return dataIn;
  }
  
  public ICommandHolder readCommandFromDataIn(DriveCommand paramDriveCommand, GetStausCommand paramGetStausCommand, LiftingArmCommand paramLiftingArmCommand, DisconnectCommand paramDisconnectCommand)
    throws IOException
  {
    return ProtocolReaderWriter.readCommandFromDataIn(paramDriveCommand, paramGetStausCommand, paramLiftingArmCommand, paramDisconnectCommand, dataIn);
  }
}
