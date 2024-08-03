package com.game.engine.audio;

import com.game.caches.GlobalCache;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.engine.scene.audio.AudioSource;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class AudioManager {
  private final HashMap<String, AudioSource> sources;
  private final ALCapabilities capabilities;
  private final long device;
  private final long context;

  public AudioManager() {
    sources = new HashMap<>();

    device = ALC11.alcOpenDevice((ByteBuffer) null);

    if (device == MemoryUtil.NULL)
      throw new IllegalStateException("Failed to open the OpenAL device.");

    ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
    if (!deviceCapabilities.OpenALC11) throw new IllegalStateException("Failed to create device capabilities.");

    context = ALC11.alcCreateContext(device, (IntBuffer) null);
    if (context == MemoryUtil.NULL)
      throw new IllegalStateException("Failed to create OpenAL context.");

    boolean useThreadLocalContext = deviceCapabilities.ALC_EXT_thread_local_context && EXTThreadLocalContext.alcSetThreadContext(
      context);

    if (!useThreadLocalContext && !ALC11.alcMakeContextCurrent(context))
      throw new IllegalStateException("Could not find thread local context or make current context current.");

    capabilities = AL.createCapabilities(deviceCapabilities, MemoryUtil::memCallocPointer);
  }

  public AudioSource load(String key, String path) {
    AudioBufferObject abo = GlobalCache.instance().audioBuffer(path);
    AudioSource aso = new AudioSource(key);
    aso.buffer(abo);
    sources.put(key, aso);
    return aso;
  }

  public AudioSource get(String key) { return sources.get(key); }

  public void setAttenuationModel(int model) { AL11.alDistanceModel(model); }

  public void dispose() {
    sources.values().forEach(AudioSource::dispose);

    if (capabilities != null) MemoryUtil.memFree(capabilities.getAddressBuffer());
    if (context != MemoryUtil.NULL) ALC11.alcDestroyContext(context);
    if (device != MemoryUtil.NULL) ALC11.alcCloseDevice(device);
  }
}
