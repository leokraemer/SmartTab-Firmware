package test;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.*;

import CommandHolders.BaseCommandHolder;
import CommandHolders.DisconnectCommand;
import CommandHolders.DriveCommand;
import CommandHolders.GetStausCommand;
import CommandHolders.ICommandHolder;
import CommandHolders.LiftingArmCommand;

public class ProtocolReaderTest
{
  private DisconnectCommand disconnectCommand = new DisconnectCommand();
  private DriveCommand driveCommand = new DriveCommand();
  private float epsilon = 1.0E-4F;
  private GetStausCommand getStausCommand = new GetStausCommand();
  private LiftingArmCommand liftingArmCommand = new LiftingArmCommand();
  private TestProtocolReader protocolReader;
  
  public ProtocolReaderTest() {}
  
  @Test(expected=IOException.class)
  public void TestEmptyStream()
    throws IOException
  {
    protocolReader = new TestProtocolReader(new byte[1]);
    protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
  }
  
  @After
  public void after()
    throws IOException
  {
    if (protocolReader != null) {
      Assert.assertEquals(0L, protocolReader.getDataIn().available());
    }
  }
  
  @Test
  public void testBoundsCheck()
  {
    Assert.assertEquals(1.0F, BaseCommandHolder.boundsCheck(1.0F, 1.0F, 2.0F), epsilon);
    Assert.assertEquals(2.0F, BaseCommandHolder.boundsCheck(3.0F, 1.0F, 2.0F), epsilon);
    Assert.assertEquals(1.0F, BaseCommandHolder.boundsCheck(-1.0F, 1.0F, 2.0F), epsilon);
  }
  
  @Test
  public void testBoundsCkeckonStream()
    throws IOException
  {
    protocolReader = new TestProtocolReader(ByteBuffer.allocate(32).putInt(ICommandHolder.Command.DRIVE.ordinal()).putFloat(-2.0F).putFloat(-3.0F).putFloat(2.0F).putFloat(3.0F).putInt(ICommandHolder.Command.LIFTINGARM.ordinal()).putFloat(110.0F).putInt(50).array());
    Object localObject = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DeiveCommand, but was " + localObject.getClass().toString(), localObject instanceof DriveCommand);
    localObject = (DriveCommand)localObject;
    Assert.assertEquals(((DriveCommand)localObject).getSpeed(), 0.0F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getVx(), -1.0F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getVy(), 1.0F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getTurn(), 1.0F, epsilon);
    localObject = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DriveCommand, but was " + localObject.getClass().toString(), localObject instanceof LiftingArmCommand);
    Assert.assertEquals(((LiftingArmCommand)localObject).getPosition(), 100.0F, epsilon);
    Assert.assertEquals(((LiftingArmCommand)localObject).getPower(), 50);
  }
  
  @Test
  public void testDisconnectCommand()
    throws IOException
  {
    protocolReader = new TestProtocolReader(ByteBuffer.allocate(4).putInt(ICommandHolder.Command.DISCONNECT.ordinal()).array());
    ICommandHolder localICommandHolder = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DriveCommand, but was " + localICommandHolder.getClass().toString(), localICommandHolder instanceof DisconnectCommand);
  }
  
  @Test
  public void testDriveStream()
    throws IOException
  {
    protocolReader = new TestProtocolReader(ByteBuffer.allocate(20).putInt(ICommandHolder.Command.DRIVE.ordinal()).putFloat(0.25F).putFloat(0.5F).putFloat(0.75F).putFloat(0.99F).array());
    Object localObject = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DeiveCommand, but was " + localObject.getClass().toString(), localObject instanceof DriveCommand);
    localObject = (DriveCommand)localObject;
    Assert.assertEquals(((DriveCommand)localObject).getSpeed(), 0.25F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getVx(), 0.5F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getVy(), 0.75F, epsilon);
    Assert.assertEquals(((DriveCommand)localObject).getTurn(), 0.99F, epsilon);
  }
  
  @Test
  public void testGetStatusCommand()
    throws IOException
  {
    protocolReader = new TestProtocolReader(ByteBuffer.allocate(4).putInt(ICommandHolder.Command.GETSTATUS.ordinal()).array());
    ICommandHolder localICommandHolder = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DriveCommand, but was " + localICommandHolder.getClass().toString(), localICommandHolder instanceof GetStausCommand);
  }
  
  @Test
  public void testLiftingArmStream()
    throws IOException
  {
    protocolReader = new TestProtocolReader(ByteBuffer.allocate(12).putInt(ICommandHolder.Command.LIFTINGARM.ordinal()).putFloat(0.25F).putInt(2).array());
    ICommandHolder localICommandHolder = protocolReader.readCommandFromDataIn(driveCommand, getStausCommand, liftingArmCommand, disconnectCommand);
    Assert.assertTrue("Expect result to be of type DriveCommand, but was " + localICommandHolder.getClass().toString(), localICommandHolder instanceof LiftingArmCommand);
    Assert.assertEquals(((LiftingArmCommand)localICommandHolder).getPosition(), 0.25F, epsilon);
    Assert.assertEquals(((LiftingArmCommand)localICommandHolder).getPower(), 2);
  }
}
