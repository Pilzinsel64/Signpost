package gollorum.signpost.network.handlers;

import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gollorum.signpost.blocks.tiles.BigPostPostTile;
import gollorum.signpost.management.PostHandler;
import gollorum.signpost.network.NetworkHandler;
import gollorum.signpost.network.messages.SendBigPostBasesMessage;
import gollorum.signpost.util.BigBaseInfo;
import gollorum.signpost.util.Sign;
import gollorum.signpost.util.Sign.OverlayType;

public class SendBigPostBasesHandler implements IMessageHandler<SendBigPostBasesMessage, IMessage> {

    @Override
    public IMessage onMessage(SendBigPostBasesMessage message, MessageContext ctx) {
        TileEntity tile = message.pos.getTile();
        BigBaseInfo bases;
        if (tile instanceof BigPostPostTile) {
            BigPostPostTile postTile = (BigPostPostTile) tile;
            postTile.isWaystone();
            bases = postTile.getBases();
        } else {
            bases = PostHandler.getBigPosts()
                .get(message.pos);
            if (bases == null) {
                bases = new BigBaseInfo(new Sign(null), null);
                PostHandler.getBigPosts()
                    .put(message.pos, bases);
            }
        }
        bases.sign.rotation = message.baserot;
        bases.sign.flip = message.flip;
        bases.sign.base = PostHandler.getForceWSbyName(message.base);
        bases.sign.overlay = OverlayType.get(message.overlay);
        bases.sign.point = message.point;
        bases.description = message.description;
        bases.sign.paint = message.paint;
        bases.postPaint = message.postPaint;

        switch (message.paintObjectIndex) {
            case 1:
                bases.paintObject = bases;
                bases.awaitingPaint = true;
                break;
            case 2:
                bases.paintObject = bases.sign;
                bases.awaitingPaint = true;
                break;
            default:
                bases.paintObject = null;
                bases.awaitingPaint = false;
                break;
        }
        if (ctx.side.equals(Side.SERVER)) {
            ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos.x, message.pos.y, message.pos.z)
                .markDirty();
            NetworkHandler.netWrap.sendToAll(message);
        }
        return null;
    }

}
