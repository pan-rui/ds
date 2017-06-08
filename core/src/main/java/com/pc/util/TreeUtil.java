package com.pc.util;

import com.pc.core.DataConstants;
import com.pc.core.TableConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TreeUtil {

	public static final String ID = "id";
	public static final String PARENT_ID = "parentId";
	public static final String TREE_CODE = "treeCode";

	public static List<Map<String, Object>> getRegionTrees(Boolean isContains,List<Map<String, Object>> listProjects, 
			List<Map<String, Object>> listProjectPeriods,
			List<Map<String, Object>> listBuildings, List<Map<String, Object>> listFloors,
			List<Map<String, Object>> listRooms) {
		List<Map<String, Object>> regionTrees = new ArrayList<Map<String, Object>>();
		int index = 0;

		List<Map<String, Object>> listRoomsByFloor = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listFloorsByBuilding = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listBuildingsByPeriod = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listPeriodsByProject = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapFloors = null;
		Map<String, Object> mapFloor = null;
		Map<String, Object> floorEntity = null;
		Map<String, Object> mapRooms = null;
		Map<String, Object> mapRoom = null;
		Map<String, Object> mapBuildings = null;
		Map<String, Object> mapBuilding = null;
		Map<String, Object> mapPeriods = null;
		Map<String, Object> mapPeriod = null;
		Map<String, Object> mapProject = null;

		int floor = 0;
		String buildingFloor = null;
		String periodID = null;
		String buildingID = null;
		String projectID = null;
		

		if (listRooms != null && listRooms.size() > 0) {
			mapRooms = new HashMap<String, Object>();
			for (index = 0; index < listRooms.size(); index++) {
				mapRoom = listRooms.get(index);
				if (mapRoom == null || mapRoom.isEmpty()) {
					continue;
				}

				buildingID = (String) mapRoom.get(TableConstants.ProjectHousehold.buildingId.name());

				if (buildingID == null) {
					continue;
				}

				floor = (int) mapRoom.get(TableConstants.ProjectHousehold.floor.name());

				buildingFloor = String.format("%s_%03d", buildingID, floor);

				listRoomsByFloor = null;

				if (mapRooms.containsKey(buildingFloor)) {
					listRoomsByFloor = (List<Map<String, Object>>) mapRooms.get(buildingFloor);
				}

				if (listRoomsByFloor == null) {
					listRoomsByFloor = new ArrayList<Map<String, Object>>();

					mapRooms.put(buildingFloor, listRoomsByFloor);
				}

				listRoomsByFloor.add(mapRoom);
			}
		}

		if (listFloors != null && listFloors.size() > 0) {
			mapFloors = new HashMap<String, Object>();
			for (index = 0; index < listFloors.size(); index++) {
				mapFloor = listFloors.get(index);

				if (mapFloor == null) {
					continue;
				}

				floorEntity = new HashMap<String, Object>();

				floorEntity.putAll(mapFloor);

				buildingID = (String) mapFloor.get(TableConstants.ProjectHousehold.buildingId.name());

				if (buildingID == null) {
					continue;
				}

				floor = (int) mapFloor.get(TableConstants.ProjectHousehold.floor.name());

				buildingFloor = String.format("%s_%03d", buildingID, floor);

				listFloorsByBuilding = null;

				if (mapFloors.containsKey(buildingID)) {
					listFloorsByBuilding = (List<Map<String, Object>>) mapFloors.get(buildingID);
				}

				if (listFloorsByBuilding == null) {
					listFloorsByBuilding = new ArrayList<Map<String, Object>>();

					mapFloors.put(buildingID, listFloorsByBuilding);
				}

				if (mapRooms.containsKey(buildingFloor)) {
					listRoomsByFloor = (List<Map<String, Object>>) mapRooms.get(buildingFloor);

					Collections.sort(listRoomsByFloor, new Comparator() {
						public int compare(Object arg0, Object arg1) {
							Map<String, Object> map1 = (Map<String, Object>) arg0;
							Map<String, Object> map2 = (Map<String, Object>) arg1;
							int result = 0;

							int room1 = 0;
							int room2 = 0;

							if (map1.get(TableConstants.ProjectHousehold.room.name()) != null) {
								room1 = (int) map1.get(TableConstants.ProjectHousehold.room.name());
							}

							if (map2.get(TableConstants.ProjectHousehold.room.name()) != null) {
								room2 = (int) map2.get(TableConstants.ProjectHousehold.room.name());
							}

							result = room1 - room2;

							return result;
						}
					});
					
					if(isContains){
						floorEntity.put(TableConstants.ProjectHousehold.roomName.name(), DataConstants.REGION_FLOOR_KEY);
						listRoomsByFloor.add(0, floorEntity);
					}

					mapFloor.put(DataConstants.REGION_ROOM_LIST_TYPE_KEY, listRoomsByFloor);
				}

				listFloorsByBuilding.add(mapFloor);
			}
		} else {
			if ((listProjectPeriods == null || (listProjectPeriods != null && listProjectPeriods.size() <= 0))
					&& (listBuildings == null || (listBuildings != null && listBuildings.size() <= 0))) {
				regionTrees = listRoomsByFloor;
			}
		}

		if (listBuildings != null && listBuildings.size() > 0) {

			mapBuildings = new HashMap<String, Object>();
			for (index = 0; index < listBuildings.size(); index++) {
				mapBuilding = listBuildings.get(index);

				periodID = (String) mapBuilding.get(TableConstants.ProjectBuilding.projectPeriodId.name());

				buildingID = (String) mapBuilding.get(TableConstants.ProjectBuilding.id.name());

				listBuildingsByPeriod = null;

				if (mapBuildings.containsKey(periodID)) {
					listBuildingsByPeriod = (List<Map<String, Object>>) mapBuildings.get(periodID);
				}

				if (listBuildingsByPeriod == null) {
					listBuildingsByPeriod = new ArrayList<Map<String, Object>>();

					mapBuildings.put(periodID, listBuildingsByPeriod);
				}

				if (mapFloors.containsKey(buildingID)) {

					listFloorsByBuilding = (List<Map<String, Object>>) mapFloors.get(buildingID);
					
					Collections.sort(listFloorsByBuilding, new Comparator() {
						public int compare(Object arg0, Object arg1) {
							Map<String, Object> map1 = (Map<String, Object>) arg0;
							Map<String, Object> map2 = (Map<String, Object>) arg1;

							int result = 0;

							int floor1 = 0;
							int floor2 = 0;

							if (map1.get(TableConstants.ProjectHousehold.floor.name()) != null) {
								floor1 = (int) map1.get(TableConstants.ProjectHousehold.floor.name());
							}

							if (map2.get(TableConstants.ProjectHousehold.floor.name()) != null) {
								floor2 = (int) map2.get(TableConstants.ProjectHousehold.floor.name());
							}

							result = floor1 - floor2;

							return result;
						}
					});
					
					if(isContains){
						Map<String, Object> building=new HashMap<String, Object>();
						building.put(TableConstants.ProjectHousehold.roomName.name(), DataConstants.REGION_BUILDING_KEY);
						building.put(TableConstants.ProjectHousehold.id.name(), mapBuilding.get(TableConstants.ProjectBuilding.id.name()));
						listFloorsByBuilding.add(0,building);
					}
					
					mapBuilding.put(DataConstants.REGION_FLOOR_LIST_TYPE_KEY, listFloorsByBuilding);
				}
				
				listBuildingsByPeriod.add(mapBuilding);
			}
		} else {
			if (listProjectPeriods == null || (listProjectPeriods != null && listProjectPeriods.size() > 0)) {
				regionTrees = listFloorsByBuilding;
			}
		}
		
		if (listProjectPeriods != null && listProjectPeriods.size() > 0) {

			mapPeriods = new HashMap<String, Object>();
			for (index = 0; index < listProjectPeriods.size(); index++) {
				mapPeriod = listProjectPeriods.get(index);

				projectID = (String) mapPeriod.get(TableConstants.ProjectPeriod.projectId.name());

				periodID = (String) mapPeriod.get(TableConstants.ProjectPeriod.id.name());

				listPeriodsByProject = null;

				if (mapPeriods.containsKey(projectID)) {
					listPeriodsByProject = (List<Map<String, Object>>) mapPeriods.get(projectID);
				}

				if (listPeriodsByProject == null) {
					listPeriodsByProject = new ArrayList<Map<String, Object>>();
					mapPeriods.put(projectID, listPeriodsByProject);
				}

				if (mapBuildings.containsKey(periodID)) {

					listBuildingsByPeriod = (List<Map<String, Object>>) mapBuildings.get(periodID);
					
					Collections.sort(listBuildingsByPeriod, new Comparator() {
						public int compare(Object arg0, Object arg1) {
							Map<String, Object> map1 = (Map<String, Object>) arg0;
							Map<String, Object> map2 = (Map<String, Object>) arg1;

							int result = 0;

							int building1 = 0;
							int building2 = 0;

							if (map1.get(TableConstants.ProjectBuilding.buildingSno.name()) != null) {
								building1 = (int) map1.get(TableConstants.ProjectBuilding.buildingSno.name());
							}

							if (map2.get(TableConstants.ProjectBuilding.buildingSno.name()) != null) {
								building2 = (int) map2.get(TableConstants.ProjectBuilding.buildingSno.name());
							}

							result = building1 - building2;

							return result;
						}
					});
					
					if(isContains){
						Map<String, Object> period=new HashMap<String, Object>();
						period.put(TableConstants.ProjectBuilding.buildingName.name(), DataConstants.REGION_PERIOD_KEY);
						period.put(TableConstants.ProjectBuilding.id.name(), mapPeriod.get(TableConstants.ProjectPeriod.id.name()));
						listBuildingsByPeriod.add(0,period);
					}

					mapPeriod.put(DataConstants.REGION_BUILDING_LIST_TYPE_KEY, listBuildingsByPeriod);
				}

				listPeriodsByProject.add(mapPeriod);
			}
		} else {
			if (listBuildingsByPeriod != null && listBuildingsByPeriod.size() > 0) {
				Collections.sort(listBuildingsByPeriod, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Map<String, Object> map1 = (Map<String, Object>) arg0;
						Map<String, Object> map2 = (Map<String, Object>) arg1;

						int result = 0;

						int building1 = 0;
						int building2 = 0;

						if (map1.get(TableConstants.ProjectBuilding.buildingSno.name()) != null) {
							building1 = (int) map1.get(TableConstants.ProjectBuilding.buildingSno.name());
						}

						if (map2.get(TableConstants.ProjectBuilding.buildingSno.name()) != null) {
							building2 = (int) map2.get(TableConstants.ProjectBuilding.buildingSno.name());
						}

						result = building1 - building2;

						return result;
					}
				});
				if(isContains){
					Map<String, Object> period=new HashMap<String, Object>();
					period.put(TableConstants.ProjectBuilding.buildingName.name(), DataConstants.REGION_PERIOD_KEY);
					period.put(TableConstants.ProjectBuilding.id.name(), listBuildingsByPeriod.get(0).get(TableConstants.ProjectBuilding.projectPeriodId.name()));
					listBuildingsByPeriod.add(0,period);
				}
				regionTrees = listBuildingsByPeriod;
			}
		}

		if (listProjects != null && listProjects.size() > 0) {

			for (index = 0; index < listProjects.size(); index++) {
				mapProject = listProjects.get(index);

				projectID = (String) mapProject.get(TableConstants.ProjectInfo.id.name());

				if (mapPeriods.containsKey(projectID)) {

					listPeriodsByProject = (List<Map<String, Object>>) mapPeriods.get(projectID);

					mapProject.put(DataConstants.REGION_PERIOD_LIST_TYPE_KEY, listPeriodsByProject);

					Collections.sort(listPeriodsByProject, new Comparator() {
						public int compare(Object arg0, Object arg1) {
							Map<String, Object> map1 = (Map<String, Object>) arg0;
							Map<String, Object> map2 = (Map<String, Object>) arg1;

							int result = 0;

							String period1 = "";
							String period2 = "";

							if (map1.get(TableConstants.ProjectPeriod.periodName.name()) != null) {
								period1 = (String) map1.get(TableConstants.ProjectPeriod.periodName.name());
							}

							if (map2.get(TableConstants.ProjectPeriod.periodName.name()) != null) {
								period2 = (String) map2.get(TableConstants.ProjectPeriod.periodName.name());
							}

							result = period1.compareTo(period2);

							return result;
						}
					});
				}

				regionTrees.add(mapProject);
			}
		}else{
			if (listPeriodsByProject != null && listPeriodsByProject.size() > 0) {
				regionTrees = listPeriodsByProject;
			}
		}

		return regionTrees;
	}

	public static List<Map<String, Object>> getTree(List<Map<String, Object>> treeList, String parentIdKey,
			String entityIdKey, String idKey, Map<String, Object> entityMap, String entityKey, String listKey) {
		List<Map<String, Object>> resultList = new LinkedList<Map<String, Object>>();
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		Map<String, Object> parentMap = new LinkedHashMap<String, Object>();
		Map<String, Object> treeMap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> children = null;
		Map<String, Object> corporate = null;

		for (Map<String, Object> org : treeList) {
			// obj.put(infoKey, org);
			Object parentId = org.get(parentIdKey);
			Object id = org.get(idKey);
			// 为实体时加入时

			if (parentId != null) {
				if (treeMap.containsKey(parentId)) {
					parentMap = (Map<String, Object>) treeMap.get(String.valueOf(parentId));

					if (parentMap.containsKey(listKey)) {
						children = (List<Map<String, Object>>) parentMap.get(listKey);
					}

					if (children == null) {
						children = new LinkedList<Map<String, Object>>();

						parentMap.put(listKey, children);
					}

					if (org.get(entityIdKey) != null) {
						corporate = (Map<String, Object>) entityMap.get(org.get(entityIdKey));
						org.put(entityKey, corporate);
					}

					children.add(org);
				} else {
					parentMap = new LinkedHashMap<String, Object>();

					children = new LinkedList<Map<String, Object>>();

					parentMap.put(listKey, children);

					treeMap.put(String.valueOf(parentId), parentMap);
				}
			} else {
				if (treeMap.containsKey(String.valueOf(id))) {
					parentMap = (Map<String, Object>) treeMap.get(String.valueOf(id));

					if (parentMap == null) {
						parentMap = new HashMap<String, Object>();
					}
				} else {
					parentMap = new HashMap<String, Object>();
				}

				if (org.get(entityIdKey) != null) {
					corporate = (Map<String, Object>) entityMap.get(org.get(entityIdKey));
					org.put(entityKey, corporate);
				}

				if (parentMap.containsKey(String.valueOf(id))) {
					children = (List<Map<String, Object>>) parentMap.get(listKey);
					if (children != null) {
						org.put(listKey, children);
					}
				}

				treeMap.put(String.valueOf(id), org);

				resultList.add(org);
			}
		}
		return resultList;
	}

	public static List<Map<String, Object>> getTree(List<Map<String, Object>> treeList, String parentIdKey,
			String entityIdKey, String idKey, Map<String, Object> entityMap, String infoKey, String entityKey,
			String listKey) {
		List<Map<String, Object>> resultList = new LinkedList<Map<String, Object>>();
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		Map<String, Object> parentMap = new LinkedHashMap<String, Object>();
		Map<String, Object> treeMap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> children = null;
		Map<String, Object> child = null;
		Map<String, Object> corporate = null;

		for (Map<String, Object> org : treeList) {
			// obj.put(infoKey, org);
			Object parentId = org.get(parentIdKey);
			Object id = org.get(idKey);
			// 为实体时加入时

			if (parentId != null) {
				if (treeMap.containsKey(parentId)) {
					parentMap = (Map<String, Object>) treeMap.get(String.valueOf(parentId));

					if (parentMap.containsKey(listKey)) {
						children = (List<Map<String, Object>>) parentMap.get(listKey);
					}

					if (children == null) {
						children = new LinkedList<Map<String, Object>>();

						parentMap.put(listKey, children);
					}

					child = new LinkedHashMap<String, Object>();

					if (org.get(entityIdKey) != null) {
						corporate = (Map<String, Object>) entityMap.get(org.get(entityIdKey));
						child.put(entityKey, corporate);
					}
					child.put(infoKey, org);

					children.add(child);
				} else {
					parentMap = new LinkedHashMap<String, Object>();

					children = new LinkedList<Map<String, Object>>();

					parentMap.put(listKey, children);

					treeMap.put(String.valueOf(parentId), parentMap);
				}
			} else {
				if (treeMap.containsKey(String.valueOf(id))) {
					parentMap = (Map<String, Object>) treeMap.get(String.valueOf(id));

					if (parentMap == null) {
						parentMap = new HashMap<String, Object>();
					}
				} else {
					parentMap = new HashMap<String, Object>();
				}

				if (org.get(entityIdKey) != null) {
					corporate = (Map<String, Object>) entityMap.get(org.get(entityIdKey));
					parentMap.put(entityKey, corporate);
				}
				parentMap.put(infoKey, org);

				treeMap.put(String.valueOf(id), parentMap);

				resultList.add(parentMap);
			}
		}
		return resultList;
	}

	public static Map<String, Object> getParentTree(List<Map<String, Object>> list, String parentId) {
		Map<String, Object> parentMap = null;

		if (parentId != null) {
			for (Map<String, Object> map : list) {
				if (parentId.equals(map.get(ID))) {
					parentMap = map;
					break;
				}
			}
		}

		return parentMap;
	}

	public static String getTreeCode(List<Map<String, Object>> list, String parentCode, String parentId) {
		int i = 1;
		String format = "%02d";
		String code = null;

		for (Map<String, Object> map : list) {
			if (parentCode != null && parentCode.length() > 0) {
				if (map.get(PARENT_ID) != null && parentId.compareTo((String) map.get(PARENT_ID)) == 0) {
					i++;
				}
			} else {
				if (parentId == null && map.get(PARENT_ID) == null) {
					i++;
				}
			}
		}

		if (parentCode != null && parentCode.split("\\" + TableConstants.SEPARATE).length >= 2) {
			format = "%03d";
		}

		if (parentCode == null) {
			code = String.format(format, i);
		} else {
			code = parentCode + TableConstants.SEPARATE + String.format(format, i);
		}

		return code;
	}

	public static List<Map<String, Object>> getTree(List<Map<String, Object>> tList,
			List<Map<String, Object>> pList,String pkey) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> procedure : pList) {
			for (Map<String, Object> type : tList) {
				if (((String) procedure.get(pkey))
						.equals((String) type.get("id"))) {
					List<Map<String, Object>> procedureList = (List<Map<String, Object>>) type.get("list");
					if (procedureList == null) {
						procedureList = new ArrayList<>();
						type.put("list", procedureList);
					}
					procedureList.add(procedure);
				}
			}
		}

		for (Map<String, Object> type : tList) {
			String parentId = (String) type.get(TableConstants.ProcedureType.parentId.name());
			if (parentId != null && !"0".equals(parentId) && !"".equals(parentId)) {
				for (Map<String, Object> t : tList)
					if (parentId.equals((String) t.get("id"))) {
						List<Map<String, Object>> children = (List<Map<String, Object>>) t.get("list");
						if (children == null) {
							children = new LinkedList<Map<String, Object>>();
							t.put("list", children);
						}
						children.add(type);
					}
			} else {
				resultList.add(type);
			}

		}
		return resultList;
	}

}
