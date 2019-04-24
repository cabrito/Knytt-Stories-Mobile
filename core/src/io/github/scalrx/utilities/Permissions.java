package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * Permissions.java
 * Created by: scalr at 5:58 PM, 4/11/19
 *
 * Allows us to grab permissions from Android. File name not set in stone, as more features may be
 * requested.
 *
 **************************************************************************************************/
public interface Permissions
{
    boolean isReadPermissionEnabled();
    boolean isWritePermissionEnabled();
}
