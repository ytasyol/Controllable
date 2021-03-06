package com.mrcrayfish.controllable.client;



import com.mrcrayfish.controllable.registry.ActionDataDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ButtonBinding
{
    private static final List<ButtonBinding> BINDINGS = new ArrayList<>();

    private int button;
    
    private final int defaultId;

    private boolean pressed;
    private int pressedTime;

    public ButtonBinding(int button)
    {
        this.button = button;
        this.defaultId = button;
        BINDINGS.add(this);
    }

    public void resetButton() {
        button = defaultId;
    }

    public int getButtonId()
    {
        return this.button;
    }

    public void setButton(int button)
    {
        this.button = button;
    }

    public boolean isButtonPressed()
    {
        return this.pressed && this.pressedTime == 0;
    }

    public boolean isButtonDown()
    {
        return this.pressed;
    }

    public static void tick()
    {
        for(ButtonBinding binding : BINDINGS)
        {
            if(binding.isButtonDown())
            {
                binding.pressedTime--;
            }
        }
    }

    public static void setButtonState(int button, boolean state)
    {
        for(ButtonBinding binding : BINDINGS)
        {
            if(binding.getButtonId() == button)
            {
                binding.pressed = state;
                binding.pressedTime = 0;
            }
        }
    }

    /**
     * Resets all buttons states. Called when a GUI is opened.
     */
    public static void resetButtonStates()
    {
        for(ButtonBinding binding : BINDINGS)
        {
            binding.pressed = false;
        }
    }

    public boolean isDefault() {
        return button == defaultId;
    }

    public boolean isInvalid() {
        return button < 0 || button > Buttons.LENGTH;
    }

    /**
     *
     * @param buttonBinding The button conflicting with this.
     * @param actionDataDescription The action associated with the button. Used to check if the action is specifically compliant with this button.
     *                   Meant to be used for custom implementations of {@link ButtonBinding}.
     * @return true if it conflicts with another ButtonBinding.
     * Only return true if applicable such as if the conflict is in the same circumstance, like riding a vehicle.
     */
    public boolean conflicts(ButtonBinding buttonBinding, ActionDataDescription actionDataDescription) {
        return button == buttonBinding.getButtonId();
    }

    public int getDefaultId()
    {
        return defaultId;
    }
}
