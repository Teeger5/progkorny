
const host = "http://localhost:8080";

function get(route : string) {
	return fetch(host + route)
}

export function post(route : string, data : Object) {
	return fetch(host + route, {
		method: "POST",
		body: JSON.stringify(data),
		headers: {
			"Content-Type": "application/json"
		}
	})
}

export function getServersFiltered(onSuccess : (x : Array<ServerData>) => void, filters : ServerFilters) {
	let url = "/servers";
	if (filters !== null) {
//		url += "?filter=" + encodeURIComponent(JSON.stringify(filters));
		const params = new URLSearchParams();
		if (filters.maxPlayersMax > 0) {
			params.append("maxPlayersMax", filters.maxPlayersMax.toString());
		}
		if (filters.partOfName.length > 0) {
			params.append("partOfName", filters.partOfName);
		}
		filters.versions.forEach(v => {
			console.log("v:" + v);
			params.append("v", v);
		});
		url += "?" + params.toString();
	}
	console.log("get servers URL: " + url);
	return get(url)
		.then(resp => resp.json() as Promise<Array<ServerData>>)
		.then(servers => {
			onSuccess(servers);
			console.log("available servers: " + JSON.stringify(servers, null, 4));
		})
}

export function getServers(onSuccess : (x : Array<ServerData>) => void) {
	getServersFiltered(onSuccess, null);
}

export function getVersions(onSuccess : (map : Map<string, number>) => void) {
	get("/versions")
		.then((resp) => {
			return resp.json()
		})
		.then((json) => {
			console.log("available versions json: " + JSON.stringify(json));
			let map = new Map<string, number>(json);
			console.log("available versions: " + JSON.stringify(Array.from(map.entries())));
			onSuccess(map);
		});
}

export interface ServerFilters {
	versions : Array<string>,
	maxPlayersMax : number,
	partOfName : string
}

export interface ServerData {
	name : string;
	address : string;
	port : number;
	version : string;
	maxPlayers : number;
	description : string;
}
