package com.modprobe.profit;

public class BottomSheet {

	public enum BottomSheetMenuType {
		ACTIVITY(R.drawable.weightlifting, "Log Activity"), SESSION(
				R.drawable.todolist, "Log Session"), WEIGHT(
				R.drawable.weighing_scale, "Log Weight");

		int resId;

		String name;

		BottomSheetMenuType(int resId, String name) {
			this.resId = resId;
			this.name = name;
		}

		public int getResId() {
			return resId;
		}

		public String getName() {
			return name;
		}
	}

	BottomSheetMenuType bottomSheetMenuType;

	public static BottomSheet to() {
		return new BottomSheet();
	}

	public BottomSheetMenuType getBottomSheetMenuType() {
		return bottomSheetMenuType;
	}

	public BottomSheet setBottomSheetMenuType(
			BottomSheetMenuType bottomSheetMenuType) {
		this.bottomSheetMenuType = bottomSheetMenuType;
		return this;
	}

}
