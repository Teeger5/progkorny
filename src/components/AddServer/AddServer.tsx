import {useState} from "react";
import {VERSION_NAMES, VERSIONS} from "../../App.tsx";

export function AddServer({ onSuccess }) {
	const [data, setData] = useState<ServerData>({
		version: 'null',
		name: '',
		description: '',
		address: '',
		port: 25565,
		maxPlayers: 20
	});
	const [msg, setMsg] = useState({
		msg: "",
		color: "transparent"
	});

	const handleInputChange = (event) => {
		const { name, value } = event.target;
		console.log(`new server data change: ${name} -> ${value}`);
		setData(prevData => ({
			...prevData,
			[name]: name === 'port' || name === 'maxPlayers' ? parseInt(value) : value
		}));
		console.log("data -> " + JSON.stringify(data));
	};

	if (VERSION_NAMES.length > 0 && data.version === 'null') {
		setData(prevData => ({
			...prevData,
			version: VERSION_NAMES[0]
		}));
	}

	const handleSubmit = (event : MouseEvent) => {
		event.preventDefault();
		console.log("new server data: " + JSON.stringify(data));
		post("/servers", data)
			.then(async resp => {
				let text = "✅ Szerver hozzáadva 😎"; //
				let color = "rgba(64, 160, 64, 0.5)";
				if (resp.status === 200) {
					onSuccess();
				}
				else if (resp.status === 409) {
					text = `❌ Ezen a címen (${data.address}) már található regisztrált szerver 😾`;
					color = "rgba(160, 64, 64, 0.5)";
				}
				else {
					text = await resp.text();
					color = "rgba(160, 64, 64, 0.5)";
				}
				setMsg({
					msg: text,
					color: color
				});
			});
	};
	return (
		<div>
			<form style={{
				display: "flex",
				flexFlow: "column",
				justifyContent: "stretch",
				alignItems: "stretch",
				alignContent: "stretch",
				gap: 8,
				padding: 16
			}}>
				<table>
					<tbody style={{
						display: "table"
					}}>
					<tr>
						<td><label>Név:</label></td>
						<td><input
							name="name"
							type="text"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td>Max játékosszám:<label></label></td>
						<td><input
							name="maxPlayers"
							type="number"
							defaultValue="20"
							onChange={handleInputChange}/></td>
					</tr>
					<tr>
						<td><label>Verzió:</label></td>
						<td><select
							name="version"
							defaultValue={VERSION_NAMES.length == 0 ? "" : VERSION_NAMES[0]}
							onChange={handleInputChange}>
							{VERSION_NAMES.map(k => (
								<option key={k} value={k}>{k}</option>
							))}
						</select></td>
					</tr>
					<tr>
						<td><label>Elérés:</label></td>
						<td style={{
							display: "flex",
						}}><input
							name="address"
							type="text"
							onChange={handleInputChange}
							style={{
								flexGrow: 1
							}}/>
							:<input
								name="port"
								type="number"
								style={{
									fontFamily: "monospace",
									width: "5em"
								}}
								defaultValue="25565"
								onChange={handleInputChange}/></td>
					</tr>
					</tbody>
				</table>
				<h3>Leírás:</h3>
				<textarea
					name="description"
					rows={8}
					onChange={handleInputChange}
					style={{
						borderRadius: 8,
						padding: 4
					}}>
				</textarea>
				<div
					style={{
						backgroundColor: "#4a4",
						cursor: "pointer",
						textAlign: "center",
						fontSize: 32,
						fontWeight: "bold",
						borderRadius: 8,
						padding: 8
					}}
					onClick={handleSubmit}>Hozzáadás</div>
				<div style={{
					backgroundColor: msg.color,
					textAlign: "center",
					fontSize: 16,
					padding: 4,
				}}>{msg.msg}</div>
			</form>
		</div>
	)
}
