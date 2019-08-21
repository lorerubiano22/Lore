# Experiment 2
library("stringr")
# setwd("/home/laura/workspace/Lore/Experiments/Exp2/")
setwd("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp2//")
# source("/home/laura/workspace/Lore/R/0.1reading.R")
source("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//R//0.1reading.R")
info <- c("Instance", "Opt dist", "Det", "Seed", "VarD", "VarT",
          "W economic", "W environment", "W social",
          "preventive", "corrective", "pDemand", "pTime",
          "BDS-opt c", "BDS-total c", "BDS-dist", "BDS-time",
          "BDS-economic c", "BDS-co2 c", "BDS-social c",
          "BDS-exp opt c", "BDS-exp total c", "BDS-exp dist", "BDS-exp time",
          "BDS-exp economic c", "BDS-exp co2 c", "BDS-exp social c",
          "BDS-relD", "BDS-relT", "BDS-rel", "BDS-checkList", 
          "BDS-SSC1", "BDS-SSC2", "BDS-SSB",
          "BSS-opt c", "BSS-total c", "BSS-dist", "BSS-time",
          "BSS-economic c", "BSS-co2 c", "BSS-social c",
          "BSS-exp opt c", "BSS-exp total c", "BSS-exp dist", "BSS-exp time",
          "BSS-exp economic c", "BSS-exp co2 c", "BSS-exp social c",
          "BSS-relD", "BSS-relT", "BSS-rel", "BSS-checkList",
          "BSS-SSC1", "BSS-SSC2", "BSS-SSB")

mat1 <- mat[,c(1,4,5,6,15,22,30,36,43,51,31,52)]
for(j in seq(5,10)){
  mat1[,j] <- as.numeric(as.character(mat1[,j]))}
sum(mat1[,11]=="false")
sum(mat1[,12]=="false")
mat1 <- mat1[,-c(11,12)]

groupLow <- subset(mat1, mat1[,3]=="0.1")
groupHigh <- subset(mat1, mat1[,3]=="1.0")

groupLow <- cbind(id = 1:dim(groupLow)[1], groupLow)
seqBDSLow <- NULL
seqBSSLow <- NULL
for(j in 1:length(unique(groupLow$Instance))){
  aux <- subset(groupLow, groupLow$Instance == unique(groupLow$Instance)[j])
  idsBDS <- aux$id[which.min(aux$'BDS-total c')]
  idsBSS <- aux$id[which.min(aux$'BSS-exp total c')]
  seqBDSLow <- c(seqBDSLow, idsBDS)
  seqBSSLow <- c(seqBSSLow, idsBSS)}
groupLowBDS <- groupLow[seqBDSLow,]
groupLowBSS <- groupLow[seqBSSLow,]

groupHigh <- cbind(id = 1:dim(groupHigh)[1], groupHigh)
seqBDSHigh <- NULL
seqBSSHigh <- NULL
for(j in 1:length(unique(groupHigh$Instance))){
  aux <- subset(groupHigh, groupHigh$Instance == unique(groupHigh$Instance)[j])
  idsBDS <- aux$id[which.min(aux$'BDS-total c')]
  idsBSS <- aux$id[which.min(aux$'BSS-exp total c')]
  seqBDSHigh <- c(seqBDSHigh, idsBDS)
  seqBSSHigh <- c(seqBSSHigh, idsBSS)}
groupHighBDS <- groupHigh[seqBDSHigh,]
groupHighBSS <- groupHigh[seqBSSHigh,]

mat2Low <- matrix(0, ncol = 8, nrow = length(unique(mat1$Instance)))
mat2Low <- data.frame(mat2Low)
mat2Low[,1] <- groupLowBDS[, 2]
mat2Low[,2] <- round(groupLowBDS[, 6],2)
mat2Low[,3] <- round(groupLowBDS[, 7],2)
mat2Low[,4] <- round(groupLowBDS[, 8],2)
mat2Low[,5] <- round((mat2Low[,3] - mat2Low[,2])/mat2Low[,2] * 100, 2)
mat2Low[,6] <- round(groupLowBSS[, 10],2)
mat2Low[,7] <- round(groupLowBSS[, 11],2)
mat2Low[,8] <- round((mat2Low[,6] - mat2Low[,3])/mat2Low[,3] * 100,2)

mat2Low <- rbind(mat2Low,c(NA, apply(mat2Low[,c(-1)],2,mean)))
colnames(mat2Low) <- c("Instance", "BDS-TC", "BDS-ETC", "BDS-rel", "ETC-TC (Gap)",
	"BSS-ETC", "BSS-rel", "BSS.ETC-BDS.ETC(Gap %)")

mat2High <- matrix(0, ncol = 8, nrow = length(unique(mat1$Instance)))
mat2High <- data.frame(mat2High)
mat2High[,1] <- groupHighBDS[, c(2)]
mat2High[,2] <- round(groupHighBDS[, 6],2)
mat2High[,3] <- round(groupHighBDS[, 7],2)
mat2High[,4] <- round(groupHighBDS[, 8],2)
mat2High[,5] <- round((mat2High[,3] - mat2High[,2])/mat2High[,2] * 100, 2)
mat2High[,6] <- round(groupHighBSS[, 10],2)
mat2High[,7] <- round(groupHighBSS[, 11],2)
mat2High[,8] <- round((mat2High[,6] - mat2High[,3])/mat2High[,3] * 100,2)

colnames(mat2High) <- colnames(mat2Low)
mat2High <- rbind(mat2High,c(NA, apply(mat2High[,c(-1)],2,mean)))