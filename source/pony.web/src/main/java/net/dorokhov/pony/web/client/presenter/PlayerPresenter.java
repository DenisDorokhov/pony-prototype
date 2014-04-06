package net.dorokhov.pony.web.client.presenter;

public interface PlayerPresenter {

	public void onVolumeChange(double aVolume);

	public void onPositionChange(double aTime);

	public void onStop();
	public void onStart();

	public void onPause();
	public void onResume();

}
