/*
 * Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of BetaSteward_at_googlemail.com.
 */
package mage.abilities.effects.common.discard;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.game.Game;
import mage.players.Player;
import mage.util.CardUtil;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class DiscardTargetEffect extends OneShotEffect {

    protected DynamicValue amount;
    protected boolean randomDiscard;

    public DiscardTargetEffect(DynamicValue amount) {
        this(amount, false);
    }

    public DiscardTargetEffect(DynamicValue amount, boolean randomDiscard) {
        super(Outcome.Discard);
        this.randomDiscard = randomDiscard;
        this.amount = amount;
    }

    public DiscardTargetEffect(int amount) {
        this(new StaticValue(amount));
    }

    /**
     *
     * @param amount amount of cards to discard
     * @param randomDiscard discard the cards by random
     *
     */
    public DiscardTargetEffect(int amount, boolean randomDiscard) {
        super(Outcome.Discard);
        this.randomDiscard = randomDiscard;
        this.amount = new StaticValue(amount);
    }

    public DiscardTargetEffect(final DiscardTargetEffect effect) {
        super(effect);
        this.amount = effect.amount.copy();
        this.randomDiscard = effect.randomDiscard;
    }

    @Override
    public DiscardTargetEffect copy() {
        return new DiscardTargetEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        for (UUID targetPlayerId : targetPointer.getTargets(game, source)) {
            Player player = game.getPlayer(targetPlayerId);
            if (player != null) {
                player.discard(amount.calculate(game, source, this), randomDiscard, source, game);
            }
        }
        return true;
    }

    @Override
    public String getText(Mode mode) {
        if (staticText != null && !staticText.isEmpty()) {
            return staticText;
        }
        StringBuilder sb = new StringBuilder();
        if (mode.getTargets().isEmpty()) {
            sb.append("that player");
        } else {
            sb.append("target ").append(mode.getTargets().get(0).getTargetName());
        }
        sb.append(" discards ");
        if (amount.toString().equals("1")) {
            sb.append(" a card");
        } else {
            sb.append(CardUtil.numberToText(amount.toString())).append(" cards");
        }
        if (randomDiscard) {
            sb.append(" at random");
        }
        String message = amount.getMessage();
        if (!message.isEmpty()) {
            sb.append(" for each ");
        }
        sb.append(message);
        return sb.toString();
    }
}
