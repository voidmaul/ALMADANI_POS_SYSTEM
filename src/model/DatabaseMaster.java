package model;

public final class DatabaseMaster {

	private DatabaseMaster() {
		throw new UnsupportedOperationException("Utility class tidak boleh diinisiasi.");
	}

	public static final String[] MERK_LIST = {"HONDA", "YAMAHA", "SUZUKI"};
	public static final String[] TAHUN_LIST = {"2026", "2025", "2024"};

	// Data Katalog Model Motor Terlengkap per Merek & Tahun
	private static final String[][] TIPE_PER_TAHUN = {
			/* HONDA */ {
			/* 2026 */ "BeAT CBS, BeAT CBS-ISS Deluxe, BeAT Deluxe Smart Key, BeAT Street, Genio CBS, Scoopy Fashion, Scoopy Prestige, Vario 125 CBS, Vario 125 CBS-ISS, Vario 160 CBS, Vario 160 ABS, Stylo 160 CBS, Stylo 160 ABS, PCX 160 CBS, PCX 160 ABS, ADV 160 CBS, ADV 160 ABS, CB150 Verza SW, CB150R Streetfire, CB150X, CRF150L, CBR150R STD, CBR150R ABS, CBR250RR STD, CBR250RR ABS QS, Monkey 125",
			/* 2025 */ "BeAT CBS, BeAT Deluxe, BeAT Street, Genio CBS, Scoopy Fashion, Scoopy Stylish, Vario 125 CBS, Vario 125 CBS-ISS, Vario 160 CBS, Vario 160 ABS, Stylo 160 CBS, PCX 160 CBS, PCX 160 ABS, ADV 160 CBS, CB150 Verza CW, CB150R SE, CRF150L, CBR150R STD, CBR250RR STD",
			/* 2024 */ "BeAT CBS, BeAT Deluxe, BeAT Street, Scoopy Fashion, Vario 125 CBS, Vario 160 CBS, PCX 160 CBS, PCX 160 ABS, ADV 160 CBS, CB150 Verza SW, CRF150L, CBR150R STD"
	},
			/* YAMAHA */ {
			/* 2026 */ "Mio M3 125, Fazzio Hybrid Neo, Fazzio Hybrid Lux, Grand Filano Hybrid Neo, Grand Filano Hybrid Lux, NMAX Neo, NMAX Neo S, NMAX Turbo, NMAX Turbo Tech MAX, Aerox Alpha Standard, Aerox Alpha CyberCity, Aerox Alpha Turbo, XMAX 250 Connected, XMAX 250 Tech Max, XSR 155, WR155 R, MT-15, MT-25, R15 Connected, R15M ABS, MX King 150, FreeGo 125 Standard, FreeGo 125 Connected",
			/* 2025 */ "Mio M3 125, Fazzio Neo, Grand Filano Neo, NMAX 155 Standard, NMAX 155 S, Aerox 155 Standard, Aerox 155 CyberCity, XMAX 250 Connected, XSR 155, WR155 R, MT-15, R15 Connected, MX King 150, FreeGo 125",
			/* 2024 */ "Mio M3 125, Fino 125 Premium, Fazzio Hybrid, FreeGo 125, NMAX 155 Standard, NMAX 155 S, Aerox 155 Standard, XSR 155, WR155 R, MX King 150"
	},
			/* SUZUKI */ {
			/* 2026 */ "Address FI, NEX II Standard, NEX Crossover, Burgman Street 125EX, Access 125, All New Satria F150, Satria Pro, GSX-R150 Keyless, GSX-R150 ABS, GSX-S150 Keyless, V-Strom 250 SX",
			/* 2025 */ "Address FI, NEX II Standard, Burgman Street 125EX, All New Satria F150, GSX-R150 Keyless, GSX-R150 ABS, V-Strom 250 SX",
			/* 2024 */ "Address FI, NEX II Standard, Satria F150, GSX-R150 Keyless, GSX-S150 Keyless"
	}
	};

	// Harga OTR Jakarta Sesuai dengan Urutan Tipe dan Tahun di Atas
	private static final int[][][] HARGA_PER_TAHUN = {
			/* HONDA */ {
			/* 2026 */ {19174000, 20469000, 20999000, 20469000, 20269000, 23584000, 24389000, 25153000, 26808000, 28342000, 32091000, 29463000, 33217000, 34592000, 39778000, 37459000, 41152000, 24404000, 34874000, 35214000, 38809000, 39214000, 43479000, 71979000, 83764000, 87988000},
			/* 2025 */ {18600000, 19800000, 19500000, 19700000, 22800000, 23500000, 24500000, 26000000, 27800000, 31500000, 28500000, 34000000, 39000000, 37000000, 24800000, 35000000, 38000000, 38500000, 70000000},
			/* 2024 */ {18000000, 19200000, 19000000, 22500000, 24000000, 27000000, 33000000, 38000000, 36000000, 23800000, 37500000, 38000000}
	},
			/* YAMAHA */ {
			/* 2026 */ {18300000, 23700000, 24400000, 28265000, 28895000, 34550000, 35155000, 39165000, 44120000, 31350000, 28530000, 39750000, 70015000, 73260000, 40265000, 41125000, 39800000, 65700000, 45000000, 45950000, 27320000, 23502600, 26015000},
			/* 2025 */ {18000000, 23000000, 27900000, 31620000, 33180000, 28330000, 28530000, 67960000, 37700000, 38660000, 38520000, 41200000, 25870000, 24100000},
			/* 2024 */ {17500000, 20150000, 22356000, 23920000, 31000000, 32500000, 27800000, 37000000, 38000000, 25500000}
	},
			/* SUZUKI */ {
			/* 2026 */ {21735000, 20480000, 21565000, 26843500, 26050000, 31300000, 35200000, 36100000, 39400000, 32620000, 60848000},
			/* 2025 */ {21200000, 20000000, 26500000, 31000000, 35500000, 39000000, 60000000},
			/* 2024 */ {20500000, 19500000, 30500000, 35000000, 32000000}
	}
	};

	// Pilihan Warna Spesifik per Tipe / Model (Disusun sejajar dengan indeks TIPE_PER_TAHUN)
	private static final String[][][] WARNA_PER_TAHUN = {
			/* HONDA */ {
			/* 2026 */ {
			"Glossy Black, Energetic Red", // BeAT CBS
			"Deluxe Black, Deluxe Green, Deluxe Blue", // BeAT CBS-ISS Deluxe
			"Deluxe Matte Black, Deluxe Matte Blue", // BeAT Deluxe Smart Key
			"Street Black, Street Silver", // BeAT Street
			"Smart Red, Smart Black", // Genio CBS
			"Fashion Brown, Fashion Blue", // Scoopy Fashion
			"Prestige White, Prestige Black", // Scoopy Prestige
			"Active Black, Matte Silver", // Vario 125 CBS
			"Advance Matte Black, Advance Matte White", // Vario 125 CBS-ISS
			"Matte Black, Matte Red", // Vario 160 CBS
			"Active White, Grande Matte White", // Vario 160 ABS
			"Royal Matte Black, Royal Matte White", // Stylo 160 CBS
			"Royal Green, Royal Matte Black", // Stylo 160 ABS
			"Matte Black, Pearl White", // PCX 160 CBS
			"Imperial Matte Blue, Wonderful White", // PCX 160 ABS
			"Matte Gunpowder Black, Matte Red", // ADV 160 CBS
			"Pearl Smoky Gray, Tough Matte White", // ADV 160 ABS
			"Masculine Black, Matte Gunpowder Black", // CB150 Verza SW
			"Machined Black, Energetic Red", // CB150R Streetfire
			"Matte Charcoal, Pearl Nightfall Blue", // CB150X
			"Extreme Black, Red", // CRF150L
			"Victory Red Black, Honda Tricolor", // CBR150R STD
			"Dominator Matte Black, Honda Racing Red", // CBR150R ABS
			"Matte Gunpowder Black Metallic, Grand Prix Red", // CBR250RR STD
			"Honda Tricolor, Katana Edition", // CBR250RR ABS QS
			"Banana Yellow, Pearl Nebula Red" // Monkey 125
	},
			/* 2025 */ {
			"Dance White, Hard Rock Black",
			"Deluxe Black, Deluxe Blue",
			"Street Black, Street Silver",
			"Smart Black, Smart Red",
			"Fashion Blue, Fashion Cream",
			"Stylish Red, Stylish Brown",
			"Advance Black, Advance Red",
			"Advance Matte Black, Advance Matte Blue",
			"Matte Black, Active Matte Red",
			"Grand Matte White, Imperial Matte Blue",
			"Royal Black, Royal White",
			"Wonderful White, Matte Black",
			"Imperial Matte Blue, Royal Matte Green",
			"Matte Gunpowder Black, Tough Matte Brown",
			"Masculine Black, Bold Red",
			"Stinger Red Black, Armored Matte Grey",
			"Extreme Black, Red",
			"Victory Red Black, Matte Black",
			"Matte Gunpowder Black, Honda Racing Red"
	},
			/* 2024 */ {
			"Dance White, Hard Rock Black",
			"Deluxe Black, Deluxe Silver",
			"Street Black",
			"Fashion Blue, Fashion Cream",
			"Advance Matte Black",
			"Matte Black, Active Red",
			"Wonderful White, Matte Black",
			"Imperial Blue, Matte Black",
			"Tough Matte Brown, Matte Black",
			"Masculine Black",
			"Extreme Black",
			"Victory Red Black"
	}
	},
			/* YAMAHA */ {
			/* 2026 */ {
			"Metallic Black, Cyan, Metallic Red",
			"Neo Mint, Neo Dull Blue, Neo Red, Neo White",
			"Lux Matte Black, Lux Prestige Silver",
			"Neo Cyan, Neo Dull Blue, Neo Red, Neo Black",
			"Lux Matte Black, Lux Matte White",
			"Magma Black, Cyan",
			"Prestige Silver, Matte Green",
			"Turbo Tech Max, Turbo",
			"Turbo Tech Max Ultimate",
			"Standard Black, Red",
			"CyberCity, Metallic Blue",
			"Turbo Ultimate",
			"Dark Petrol, Matte Black",
			"Tech Max Grey",
			"Matte Dark Blue, WGP 60th",
			"Extreme Yellow, Army Green",
			"Matte Black, Metallic Red",
			"Dark Grey, Racing Blue",
			"Matte Blue, Matte Black",
			"R15M Icon Blue, Signature Grey",
			"Black, Red",
			"Standard Dull Blue, Matte Black",
			"Connected Cyan, Black"
	},
			/* 2025 */ {
			"Metallic Black, Cyan",
			"Neo Mint, Neo Dull Blue, Neo Red",
			"Lux Matte Black, Lux Silver",
			"Standard Black, Matte Blue, Red",
			"Standard Prestige Silver, Matte Red",
			"Standard Black, Red, Silver",
			"CyberCity Blue",
			"Dark Petrol, Matte Black",
			"Matte Dark Blue, Historic White",
			"Extreme Yellow",
			"Matte Black, Red",
			"Racing Blue, Dark Grey",
			"Black, Red",
			"Dull Blue, Black"
	},
			/* 2024 */ {
			"Metallic Black, Cyan",
			"Fino Grande Matte Blue, Sporty Red",
			"Fazzio Neo Mint, Black",
			"FreeGo Connected Black, Sand",
			"Standard Black, Blue",
			"Standard Silver, Red",
			"Standard Black, Red",
			"Matte Dark Blue",
			"Extreme Yellow",
			"Black, Red"
	}
	},
			/* SUZUKI */ {
			/* 2026 */ {
			"Titan Black, Brilliant White",
			"Stronger Red, Titan Black",
			"Met. Matte Stellar Blue, Titan Black",
			"Metallic Royal Bronze, Matte Platinum Silver, Matte Black",
			"Matte Green, Pearl White, Solid Black",
			"Titan Black, Stronger Red",
			"Satria Pro Special Edition",
			"Keyless Titan Black, Met. Triton Blue",
			"ABS Met. Triton Blue",
			"Keyless Titan Black",
			"Champion Yellow No. 2, Metallic Matte Black"
	},
			/* 2025 */ {
			"Titan Black, Brilliant White",
			"Stronger Red, Titan Black",
			"Metallic Royal Bronze, Matte Black",
			"Titan Black, Stronger Red",
			"Keyless Titan Black, Met. Triton Blue",
			"ABS Met. Triton Blue",
			"Champion Yellow No. 2"
	},
			/* 2024 */ {
			"Titan Black",
			"Stronger Red",
			"Titan Black, Met. Triton Blue",
			"Keyless Titan Black",
			"Keyless Titan Black"
	}
	}
	};

	public static String[] getTipeByMerkAndTahun(int merkIdx, int tahunIdx) {
		if (merkIdx < 0 || merkIdx >= TIPE_PER_TAHUN.length || tahunIdx < 0 || tahunIdx >= TIPE_PER_TAHUN[merkIdx].length) {
			return new String[0];
		}
		return TIPE_PER_TAHUN[merkIdx][tahunIdx].split(",\\s*");
	}

	public static String[] getWarnaByMerkTahunAndTipe(int merkIdx, int tahunIdx, int tipeIdx) {
		try {
			// Mengambil daftar warna spesifik berdasarkan indeks tipe motor yang dipilih
			String warnaStr = WARNA_PER_TAHUN[merkIdx][tahunIdx][tipeIdx];
			if (warnaStr != null && !warnaStr.trim().isEmpty()) {
				return warnaStr.split(",\\s*");
			}
		} catch (Exception ignored) {}

		// Jika data warna tidak tersedia, kembalikan array kosong (tidak menampilkan apa-apa)
		return new String[0];
	}

	public static int getHargaByMerkTahunAndTipe(int merkIdx, int tahunIdx, int tipeIdx) {
		try {
			return HARGA_PER_TAHUN[merkIdx][tahunIdx][tipeIdx];
		} catch (Exception e) {
			return 20000000; // Harga default jika di luar indeks
		}
	}
}