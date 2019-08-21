# Read outputs
info <- c("Instance", "Time", "Opt dist", "Det", "Seed", "VarD", "VarT",
          "W economic", "W environment", "W social",
          "preventive", "corrective", "pDemand", "pTime",
          "BDS-opt c", "BDS-total c", "BDS-dist", "BDS-time",
          "BDS-economic c", "BDS-co2 c", "BDS-social c",
          "BDS-exp opt c", "BDS-exp total c", "BDS-exp dist", "BDS-exp time",
          "BDS-exp economic c", "BDS-exp co2 c", "BDS-exp social c",
          "BDS-relD", "BDS-relT", "BDS-rel", "BDS-checkList", 
          "BDS-SSC1", "BDS-SSC2", "BDS-SSB","BDS-Customers unserved",
          "BSS-opt c", "BSS-total c", "BSS-dist", "BSS-time",
          "BSS-economic c", "BSS-co2 c", "BSS-social c",
          "BSS-exp opt c", "BSS-exp total c", "BSS-exp dist", "BSS-exp time",
          "BSS-exp economic c", "BSS-exp co2 c", "BSS-exp social c",
          "BSS-relD", "BSS-relT", "BSS-rel", "BSS-checkList",
          "BSS-SSC1", "BSS-SSC2", "BSS-SSB", "BSS-Customers unserved")

listFiles <- list.files(".")
m <- length(listFiles)
mat <- matrix(0, nrow = m, ncol = length(info))
colnames(mat) <- info

for(i in 1:m){
  auxFile <- listFiles[i]
  mat[i,c(1:14)] <- strsplit(auxFile,"_")[[1]][c(1:3,8,11:20)]
  auxFile <- readChar(auxFile, file.info(auxFile)$size)
  iOptCosts <- str_locate_all(auxFile, "Optimization costs: ")
  iTotalCosts <- str_locate_all(auxFile, "Total costs: ")
  iDist <- str_locate_all(auxFile, "Dist: ")
  iTime <- str_locate_all(auxFile, "Time: ")
  iFuelCost <- str_locate_all(auxFile, "Fuel cost: ")
  iEconomicCost <- str_locate_all(auxFile, "Economic cost: ")
  iCo2Cost <- str_locate_all(auxFile, "Co2 cost: ")
  iSocialCost <- str_locate_all(auxFile, "Social cost: ")
  iCheckList <- str_locate_all(auxFile, "CheckList: ")
  iStochInd <- str_locate_all(auxFile, "Stochastic Indicators")
  iExpOptCosts <- str_locate_all(auxFile, "Exp. optimization costs: ")
  iExpTotalCosts <- str_locate_all(auxFile, "Exp. total costs: ")
  iExpDist <- str_locate_all(auxFile, "Exp. distance: ")
  iExpTime <- str_locate_all(auxFile, "Exp. time: ")
  iExpFuelCost <- str_locate_all(auxFile, "Exp. fuel costs: ")
  iExpEconomicCost <- str_locate_all(auxFile, "Exp. economic costs: ")
  iExpCo2Cost <- str_locate_all(auxFile, "Exp. co2 costs: ")
  iExpSocialCost <- str_locate_all(auxFile, "Exp. social costs: ")
  iExpDemand <- str_locate_all(auxFile, "Exp. demand: ")
  iRelD <- str_locate_all(auxFile, "Rel. demand: ")
  iRelT <- str_locate_all(auxFile, "Rel. time: ")
  iRel <- str_locate_all(auxFile, "Rel.: ")
  iSafetyStockC1 <- str_locate_all(auxFile, "SafetyStockC1: ")
  iSafetyStockC2 <- str_locate_all(auxFile, "SafetyStockC2: ")
  iSafetyStockB <- str_locate_all(auxFile, "SafetyStockB: ")
  iUnserved <- str_locate_all(auxFile, "Customers unserved: ")
  iCompt <- str_locate_all(auxFile, "Sol computing time: ")
  mat[i,15] <- substr(auxFile, iOptCosts[[1]][1,2], iTotalCosts[[1]][1,1]-3)
  mat[i,16] <- substr(auxFile, iTotalCosts[[1]][1,2], iDist[[1]][1,1]-3)
  mat[i,17] <- substr(auxFile, iDist[[1]][1,2], iTime[[1]][1,1]-3)
  mat[i,18] <- substr(auxFile, iTime[[1]][1,2], iFuelCost[[1]][1,1]-3)
  mat[i,19] <- substr(auxFile, iEconomicCost[[1]][1,2], iCo2Cost[[1]][1,1]-3)
  mat[i,20] <- substr(auxFile, iCo2Cost[[1]][1,2], iSocialCost[[1]][1,1]-3)  
  mat[i,21] <- substr(auxFile, iSocialCost[[1]][1,2], iCheckList[[1]][1,1]-3)
  mat[i,22] <- substr(auxFile, iExpOptCosts[[1]][1,2], iExpTotalCosts[[1]][1,1]-3)
  mat[i,23] <- substr(auxFile, iExpTotalCosts[[1]][1,2], iExpDist[[1]][1,1]-3)
  mat[i,24] <- substr(auxFile, iExpDist[[1]][1,2], iExpTime[[1]][1,1]-3)
  mat[i,25] <- substr(auxFile, iExpTime[[1]][1,2], iExpFuelCost[[1]][1,1]-3)
  mat[i,26] <- substr(auxFile, iExpEconomicCost[[1]][1,2], iExpCo2Cost[[1]][1,1]-3)
  mat[i,27] <- substr(auxFile, iExpCo2Cost[[1]][1,2], iExpSocialCost[[1]][1,1]-3)
  mat[i,28] <- substr(auxFile, iExpSocialCost[[1]][1,2], iExpDemand[[1]][1,1]-3)
  mat[i,29] <- substr(auxFile, iRelD[[1]][1,2], iRelT[[1]][1,1]-3)
  mat[i,30] <- substr(auxFile, iRelT[[1]][1,2], iRel[[1]][1,1]-3)
  mat[i,31] <- substr(auxFile, iRel[[1]][1,2], iSafetyStockC1[[1]][1,1]-3)
  mat[i,32] <- substr(auxFile, iCheckList[[1]][1,2], iStochInd[[1]][1,1]-3)  
  mat[i,33] <- substr(auxFile, iSafetyStockC1[[1]][1,2], iSafetyStockC2[[1]][1,1]-3)
  mat[i,34] <- substr(auxFile, iSafetyStockC2[[1]][1,2], iSafetyStockB[[1]][1,1]-3)
  mat[i,35] <- substr(auxFile, iSafetyStockB[[1]][1,2], iUnserved[[1]][1,1]-3) 
  mat[i,36] <- substr(auxFile, iUnserved[[1]][1,2], iCompt[[1]][1,1]-3) 
  mat[i,37] <- substr(auxFile, iOptCosts[[1]][2,2], iTotalCosts[[1]][2,1]-3)
  mat[i,38] <- substr(auxFile, iTotalCosts[[1]][2,2], iDist[[1]][2,1]-3)
  mat[i,39] <- substr(auxFile, iDist[[1]][2,2], iTime[[1]][2,1]-3)
  mat[i,40] <- substr(auxFile, iTime[[1]][2,2], iFuelCost[[1]][2,1]-3)
  mat[i,41] <- substr(auxFile, iEconomicCost[[1]][2,2], iCo2Cost[[1]][2,1]-3)
  mat[i,42] <- substr(auxFile, iCo2Cost[[1]][2,2], iSocialCost[[1]][2,1]-3)  
  mat[i,43] <- substr(auxFile, iSocialCost[[1]][2,2], iCheckList[[1]][2,1]-3)
  mat[i,44] <- substr(auxFile, iExpOptCosts[[1]][2,2], iExpTotalCosts[[1]][2,1]-3)
  mat[i,45] <- substr(auxFile, iExpTotalCosts[[1]][2,2], iExpDist[[1]][2,1]-3)
  mat[i,46] <- substr(auxFile, iExpDist[[1]][2,2], iExpTime[[1]][2,1]-3)
  mat[i,47] <- substr(auxFile, iExpTime[[1]][2,2], iExpFuelCost[[1]][2,1]-3)
  mat[i,48] <- substr(auxFile, iExpEconomicCost[[1]][2,2], iExpCo2Cost[[1]][2,1]-3)
  mat[i,49] <- substr(auxFile, iExpCo2Cost[[1]][2,2], iExpSocialCost[[1]][2,1]-3)
  mat[i,50] <- substr(auxFile, iExpSocialCost[[1]][2,2], iExpDemand[[1]][2,1]-3)
  mat[i,51] <- substr(auxFile, iRelD[[1]][2,2], iRelT[[1]][2,1]-3)
  mat[i,52] <- substr(auxFile, iRelT[[1]][2,2], iRel[[1]][2,1]-3)
  mat[i,53] <- substr(auxFile, iRel[[1]][2,2], iSafetyStockC1[[1]][2,1]-3)
  mat[i,54] <- substr(auxFile, iCheckList[[1]][2,2], iStochInd[[1]][2,1]-3)
  mat[i,55] <- substr(auxFile, iSafetyStockC1[[1]][2,2], iSafetyStockC2[[1]][2,1]-3)
  mat[i,56] <- substr(auxFile, iSafetyStockC2[[1]][2,2], iSafetyStockB[[1]][2,1]-3)
  mat[i,57] <- substr(auxFile, iSafetyStockB[[1]][2,2], iUnserved[[1]][2,1]-3) 
  mat[i,58] <- substr(auxFile, iUnserved[[1]][2,2], iCompt[[1]][2,1]-3) 
}
mat <- as.data.frame(mat)