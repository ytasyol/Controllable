package com.mrcrayfish.controllable.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.LocaleUtil;
import com.mrcrayfish.controllable.Reference;
import com.mrcrayfish.controllable.event.AvailableActionsEvent;
import com.mrcrayfish.controllable.event.RenderAvailableActionsEvent;
import com.mrcrayfish.controllable.event.RenderPlayerPreviewEvent;
import com.mrcrayfish.controllable.registry.ControllableButtons;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class RenderEvents
{
    private static final ResourceLocation CONTROLLER_BUTTONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/buttons.png");

    private Map<Integer, Action> actions = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(event.phase == TickEvent.Phase.START && mc.player != null && !mc.gameSettings.hideGUI)
        {
            this.actions = new HashMap<>();

            if(mc.currentScreen instanceof ContainerScreen)
            {
                if(mc.player.inventory.getItemStack().isEmpty())
                {
                    ContainerScreen container = (ContainerScreen) mc.currentScreen;
                    if(container.getSlotUnderMouse() != null)
                    {
                        Slot slot = container.getSlotUnderMouse();
                        if(slot.getHasStack())
                        {
                            this.actions.put(Buttons.A, new Action(I18n.format("controllable.action.pickup_stack"), Action.Side.LEFT));
                            this.actions.put(Buttons.X, new Action(I18n.format("controllable.action.pickup_item"), Action.Side.LEFT));
                            this.actions.put(Buttons.B, new Action(I18n.format("controllable.action.quick_move"), Action.Side.LEFT));
                        }
                        if (!mc.player.inventory.getCurrentItem().isEmpty() && !mc.player.abilities.isCreativeMode && !mc.player.isSpectator())
                            this.actions.put(ControllableButtons.ButtonActions.DROP_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.drop_item_held"), Action.Side.LEFT));
                    }
                }
                else
                {
                    this.actions.put(Buttons.A, new Action(I18n.format("controllable.action.place_stack"), Action.Side.LEFT));
                    this.actions.put(Buttons.X, new Action(I18n.format("controllable.action.place_item"), Action.Side.LEFT));

                    if (!mc.player.abilities.isCreativeMode && !mc.player.isSpectator())
                        this.actions.put(ControllableButtons.ButtonActions.DROP_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.drop_item"), Action.Side.LEFT));
                }

                this.actions.put(Buttons.Y, new Action(I18n.format("controllable.action.close_inventory"), Action.Side.RIGHT));
            }
            else if(mc.currentScreen == null)
            {
                boolean blockHit = mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK;
                boolean canOpenBlock = false;
                if(blockHit)
                {
                    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) mc.objectMouseOver;
                    canOpenBlock = mc.world.getBlockState(blockRayTraceResult.getPos()).getBlock() instanceof ContainerBlock;
                }

                if(!mc.player.isHandActive())
                {
                    if(blockHit)
                    {
                        this.actions.put(ControllableButtons.ButtonActions.ATTACK.getButton().getButtonId(), new Action(I18n.format("controllable.action.break"), Action.Side.RIGHT));
                    }
                    else
                    {
                        this.actions.put(ControllableButtons.ButtonActions.ATTACK.getButton().getButtonId(), new Action(I18n.format("controllable.action.attack"), Action.Side.RIGHT));
                    }
                }

                ItemStack offHandStack = mc.player.getHeldItemOffhand();
                if(offHandStack.getUseAction() != UseAction.NONE)
                {
                    switch(offHandStack.getUseAction())
                    {
                        case EAT:
                            if(mc.player.getFoodStats().needFood())
                            {
                                this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.eat"), Action.Side.RIGHT));
                            }
                            break;
                        case DRINK:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.drink"), Action.Side.RIGHT));
                            break;
                        case BLOCK:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.block"), Action.Side.RIGHT));
                            break;
                        case BOW:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.pull_bow"), Action.Side.RIGHT));
                            break;
                    }
                }

                ItemStack currentItem = mc.player.inventory.getCurrentItem();
                if(currentItem.getUseAction() != UseAction.NONE)
                {
                    switch(currentItem.getUseAction())
                    {
                        case EAT:
                            if(mc.player.getFoodStats().needFood())
                            {
                                this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.eat"), Action.Side.RIGHT));
                            }
                            break;
                        case DRINK:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.drink"), Action.Side.RIGHT));
                            break;
                        case BLOCK:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.block"), Action.Side.RIGHT));
                            break;
                        case BOW:
                            this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.pull_bow"), Action.Side.RIGHT));
                            break;
                    }
                }
                else if(currentItem.getItem() instanceof BlockItem)
                {
                    if(blockHit)
                    {
                        //TODO figure out logic to determine if block can be placed.
                        /*BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) mc.objectMouseOver;
                        BlockItem item = (BlockItem) currentItem.getItem();
                        ItemUseContext itemUseContext = new ItemUseContext(mc.player, Hand.MAIN_HAND, blockRayTraceResult);
                        BlockItemUseContext blockItemUseContext = new BlockItemUseContext(itemUseContext);
                        blockItemUseContext = item.getBlockItemUseContext(blockItemUseContext);
                        if(blockItemUseContext != null)
                        {
                            BlockState state = item.getStateForPlacement(blockItemUseContext);
                            if(state != null)
                            {
                                actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.place_block"), Action.Side.RIGHT));
                            }
                        }*/
                        this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.place_block"), Action.Side.RIGHT));
                    }
                }
                else if(!currentItem.isEmpty() && !mc.player.isHandActive())
                {
                    this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.use_item"), Action.Side.RIGHT));
                }

                if(!mc.player.isSneaking() && blockHit && canOpenBlock && !mc.player.isHandActive())
                {
                    this.actions.put(ControllableButtons.ButtonActions.USE_ITEM.getButton().getButtonId(), new Action(I18n.format("controllable.action.interact"), Action.Side.RIGHT));
                }

                //actions.put(Buttons.A, new Action(I18n.format("controllable.action.jump"), Action.Side.LEFT)); //TODO make a verbose action config option

                this.actions.put(ControllableButtons.ButtonActions.INVENTORY.getButton().getButtonId(), new Action(I18n.format("controllable.action.inventory"), Action.Side.LEFT));

                if(!mc.player.getHeldItemOffhand().isEmpty() || !mc.player.inventory.getCurrentItem().isEmpty())
                {
                    //actions.put(Buttons.X, new Action(I18n.format("controllable.action.swap_hands"), Action.Side.LEFT));  //TODO make a verbose action config option
                }

                if ((!mc.player.getFoodStats().needFood() || mc.player.isCreative() || mc.player.abilities.isFlying ) && !mc.player.isSprinting() && !Controllable.getOptions().isToggleSprint()) {
                    actions.put(ControllableButtons.ButtonActions.SPRINT.getButton().getButtonId(), new Action(I18n.format("controllable.action.sprint"), Action.Side.RIGHT));
                } else {
                    if (Controllable.getOptions().isToggleSprint()) {
                        actions.put(ControllableButtons.ButtonActions.SPRINT.getButton().getButtonId(), new Action(I18n.format("controllable.action.toggleSprint", LocaleUtil.booleanLocale(Controllable.getInput().isSprinting())), Action.Side.RIGHT));
                    }
                }

                if(mc.player.isPassenger())
                {
                    this.actions.put(ControllableButtons.ButtonActions.SNEAK.getButton().getButtonId(), new Action(I18n.format("controllable.action.dismount"), Action.Side.RIGHT));
                }
                else {
                    actions.put(ControllableButtons.ButtonActions.SNEAK.getButton().getButtonId(), new Action(I18n.format("controllable.action.sneak", LocaleUtil.booleanLocale(Controllable.getInput().isSneaking())), Action.Side.RIGHT));
                }

                if(!mc.player.inventory.getCurrentItem().isEmpty())
                {
                    this.actions.put(ControllableButtons.ButtonActions.DROP_ITEM.getButtonId(), new Action(I18n.format("controllable.action.drop_item"), Action.Side.LEFT));
                }
            }

            MinecraftForge.EVENT_BUS.post(new AvailableActionsEvent(this.actions));
        }
    }

    @SubscribeEvent
    public void onRenderScreen(TickEvent.RenderTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        if(Controllable.getInput().getLastUse() <= 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.gameSettings.hideGUI)
            return;

        if(Controllable.getController() == null)
            return;

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();

        if(!MinecraftForge.EVENT_BUS.post(new RenderAvailableActionsEvent()))
        {
            IngameGui guiIngame = mc.ingameGUI;
            boolean isChatVisible = mc.currentScreen == null && guiIngame.getChatGUI().drawnChatLines.stream().anyMatch(chatLine -> guiIngame.getTicks() - chatLine.getUpdatedCounter() < 200);

                int leftIndex = 0;
                int rightIndex = 0;
                for(Integer button : this.actions.keySet())
                {
                    Action action = this.actions.get(button);
                    Action.Side side = action.getSide();

                int remappedButton = button;
                Controller controller = Controllable.getController();
                Mappings.Entry mapping = controller.getMapping();
                if(mapping != null)
                {
                    remappedButton = mapping.remap(button);
                }

                int texU = remappedButton * 13;
                int texV = Controllable.getOptions().getControllerType().ordinal() * 13;
                int size = 13;

                int x = side == Action.Side.LEFT ? 5 : mc.getMainWindow().getScaledWidth() - 5 - size;
                int y = mc.getMainWindow().getScaledHeight() + (side == Action.Side.LEFT ? leftIndex : rightIndex) * -15 - size - 5;

                mc.getTextureManager().bindTexture(CONTROLLER_BUTTONS);

                if(isChatVisible && side == Action.Side.LEFT && leftIndex >= 2)
                    continue;


                /* Draw buttons icon */
                Widget.blit(matrixStack, x, y, texU, texV, size, size, 256, 256);

                /* Draw description text */
                if(side == Action.Side.LEFT)
                {
                    mc.fontRenderer.drawString(matrixStack, action.getDescription(), x + 18, y + 3, Color.WHITE.getRGB());
                    leftIndex++;
                }
                else
                {
                    int width = mc.fontRenderer.getStringWidth(action.getDescription());
                    mc.fontRenderer.drawString(matrixStack, action.getDescription(), x - 5 - width, y + 3, Color.WHITE.getRGB());
                    rightIndex++;
                }
            }
        }

        if(mc.player != null && mc.currentScreen == null && Controllable.getOptions().isRenderMiniPlayer())
        {
            if(!MinecraftForge.EVENT_BUS.post(new RenderPlayerPreviewEvent()))
            {
                InventoryScreen.drawEntityOnScreen(20, 45, 20, 0, 0, mc.player);
            }
        }

        matrixStack.pop();
    }
}
