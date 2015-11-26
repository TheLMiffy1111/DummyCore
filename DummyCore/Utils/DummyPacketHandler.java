package DummyCore.Utils;

import DummyCore.Core.CoreInitialiser;
import DummyCore.Events.DummyEvent_OnPacketRecieved;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

//Internal
@ChannelHandler.Sharable
public class DummyPacketHandler implements IMessageHandler<DummyPacketIMSG, IMessage> {

	@Override
	public IMessage onMessage(DummyPacketIMSG message, MessageContext ctx) 
	{
		Side s = ctx.side;
		if(s == Side.CLIENT)
		{
			MinecraftForge.EVENT_BUS.post(new DummyEvent_OnPacketRecieved(s, message.dataStr, CoreInitialiser.proxy.getPlayerOnSide(ctx.getClientHandler())));
		}else
		{
			MinecraftForge.EVENT_BUS.post(new DummyEvent_OnPacketRecieved(s, message.dataStr, CoreInitialiser.proxy.getPlayerOnSide(ctx.getServerHandler())));
		}
		return null;
	}
	
	public static void sendToAll(DummyPacketIMSG message)
	{
		CoreInitialiser.network.sendToAll(message);
	}
	
	public static void sendToAllAround(DummyPacketIMSG message, TargetPoint pnt)
	{
		CoreInitialiser.network.sendToAllAround(message, pnt);
	}
	
	public static void sendToAllAround(DummyPacketIMSG message, int dim)
	{
		CoreInitialiser.network.sendToDimension(message, dim);
	}
	
	public static void sendToPlayer(DummyPacketIMSG message, EntityPlayerMP player)
	{
		CoreInitialiser.network.sendTo(message, player);
	}
	
	public static void sendToServer(DummyPacketIMSG message)
	{
		CoreInitialiser.network.sendToServer(message);
	}

}