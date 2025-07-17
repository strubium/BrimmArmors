package interactive.blackout.brimm.common.network;

import interactive.blackout.brimm.BrimmArmors;
import interactive.blackout.brimm.common.network.packets.RequestCraftItem;
import interactive.blackout.brimm.common.network.packets.SetWorkbenchScreenS2C;

public class NetworkDispatcher extends AbstractDispatcher {

    public NetworkDispatcher() {
        super(BrimmArmors.MOD_ID);
    }

    @Override
    public void register() {
        register(RequestCraftItem.class, RequestCraftItem::read, RequestCraftItem::write, RequestCraftItem::handle);
        register(SetWorkbenchScreenS2C.class, SetWorkbenchScreenS2C::read, SetWorkbenchScreenS2C::write, SetWorkbenchScreenS2C::handle);
    }

}