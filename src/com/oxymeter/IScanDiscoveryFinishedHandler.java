package com.neuroelectrics;

import java.util.ArrayList;

public interface IScanDiscoveryFinishedHandler {
	void onScanNeighborhoodFinished(ArrayList<String> mPairedDevicesArrayAdapter, ArrayList<String> mNewDevicesArrayAdapter);
}
