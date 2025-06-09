package concord.common.network;

import concord.Concord;
import concord.common.network.packets.RequestCraftItem;
import concord.common.network.packets.SetWorkbenchScreenS2C;

public class NetworkDispatcher extends AbstractDispatcher {

    public NetworkDispatcher() {
        super(Concord.MOD_ID);
    }

    @Override
    public void register() {
        register(RequestCraftItem.class, RequestCraftItem::read, RequestCraftItem::write, RequestCraftItem::handle);
        register(SetWorkbenchScreenS2C.class, SetWorkbenchScreenS2C::read, SetWorkbenchScreenS2C::write, SetWorkbenchScreenS2C::handle);
    }

}