# Experiment 3
library("stringr")
setwd("/home/laura/workspace/Lore/Experiments/Exp3/")
# setwd("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//outputs//Exp3//")
source("/home/laura/workspace/Lore/R/0.1reading.R")
# source("C://Users//usuario//Desktop//eclipse//workspace//Lore0818//R//0.1reading.R")
info <- c("Instance", "Seed", "pDemand", "pTime",
          "BSS-exp opt c", 
          "BSS-relD", "BSS-relT", "BSS-rel", "BSS-checkList",
          "BSS-SSC1", "BSS-SSC2", "BSS-SSB")

mat1 <- mat[,c(1,4,12,13,42,49,50,51,52)]
for(j in seq(5,8)){
  mat1[,j] <- as.numeric(as.character(mat1[,j]))}
sum(mat1[,9]=="false")
mat1 <- mat1[,-c(9)]

# for each instance, pDemand, pTime .. min BSS-exp
instanceLevels <- levels(mat1$Instance)
pDLevels <- levels(mat1$pDemand)
pTLevels <- levels(mat1$pTime)
mat1 <-cbind(mat1, id = 1:dim(mat1)[1])
indexs <- NULL

for(i in 1:length(instanceLevels)){
  for(j in 1:length(pDLevels)){
    for(k in 1:length(pTLevels)){
      aux <- subset(mat1, mat1[,1] == instanceLevels[i] & mat1[,3] == pDLevels[j] & mat1[,4] == pTLevels[k])
      indexs <- c(indexs,aux$id[which.min(aux$`BSS-exp opt c`)])
    }
  }
}

results <- mat1[indexs,]

par(mfrow = c(2,3))
for(i in 1:length(instanceLevels)){
  aux <- subset(results, results[,1] == instanceLevels[i])
  plot(0,xlim=c(0.45,0.9), ylim = c(min(aux$`BSS-exp opt c`), max(aux$`BSS-exp opt c`)), xaxt="n",
       xlab="pDemand")
  if(i==1){
    legend("topleft",legend = c(0.45,0.6,0.75,0.9), lty=1:length(pTLevels), col=1:length(pTLevels), c(0.45,0.6,0.75,0.9))}
  axis(1,c(0.45,0.6,0.75,0.9))
  for(j in 1:length(pTLevels)){
    aux2 <- subset(aux, aux[,4] == pTLevels[j])
    lines(as.numeric(as.character(aux2$pDemand)), aux2$`BSS-exp opt c`, lty=j, col=j, pch=j)
  }
}