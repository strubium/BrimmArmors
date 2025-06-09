package concord.common;

import concord.common.recipes.RecipesManager;

public class CommonProxy {

    public void preInit() {

    }

    public void init() {
        RecipesManager.init();
    }

    public void client() {

    }

    public void server() {

    }

}
